#!/bin/bash

function checkStatus() {
	if [ $1 != 0 ]
		then
			echo "-=-=- $2 =-=-=-"
		exit
	else
		echo "=========ok========="
	fi
}


# 配置 相关------------------------------------------------------------------------

#svn 检出path
svnpath="${1}"
svnUsername="${2}"
svnPwd="${3}"

# 项目检出路径
targetpath="${4}"



PROVISIONING_FILE_PATH="${5}"
# 证书路径
p12path="${6}"
p12pwd="${7}"

if [[ ${7} == "isEmptyPwd" ]]; then
	#statements
	p12pwd=""
fi


# 编译选项
CONFIGURATION="${8}"
# 编译target
targetname="${9}"

# keychain password
keychainPwd="${10}"

# ipa export path
ipaExportPath="${11}"

# plist export path
plistExportPath="${12}"
dowloadBaseUrl="${13}"


echo "svnpath: ${1}"
echo "username: ${2}"
echo "pwd: ${3}"
echo "targetpath: ${4}"
echo "PROVISIONING_FILE_PATH: ${5}"
echo "p12path: ${6}"
echo "p12pwd: ${7}"
echo "CONFIGURATION: ${8}"
echo "targetname: ${9}"
echo "keychainPwd: ${10}"


# 后期生成变量

CODE_SIGN_IDENTITY=""
provisitionUUID=""
hascocoapod="1"
appName=""
bundleId=""


# svn 相关------------------------------------------------------------------------

function checkoutFromSVN() {
	svn checkout --force ${svnpath}  --username ${svnUsername} --password ${svnPwd} ${targetpath}
	checkStatus $? "检出错误"
}

function updateSVN() {
	svn update
	checkStatus $? "更新失败"
}

# pod 相关------------------------------------------------------------------------

function installPod() {
	cd ${targetpath}
	pod install --no-repo-update
	checkStatus $? "安装pod失败"
}

function updatePod() {
	cd ${targetpath}
	pod update --no-repo-update
	checkStatus $? "update pod失败"
}

# p12证书 相关------------------------------------------------------------------------

function loadCertificate() {
	# 解锁keychain
	# security unlock-keychain -p '${keychainPwd}' ~/Library/Keychains/login.keychain
	# 列举keychain
	# security list-keychains -s ~/Library/Keychains/login.keychain
	# 导入证书
	security import ${p12path} -k ~/Library/Keychains/login.keychain -P "${p12pwd}" -T /usr/bin/codesign

	checkStatus $? "导入证书失败"

	# 获取证书名称列表
	listString=`/usr/bin/security find-identity -v -p codesigning` 
	listString=`echo $listString|sed 's/)" /)"*/g'`
	oldIFS="$IFS"
	IFS="*"
	IFS=${IFS:0:1} # this is useful to format your code with tabs
	list=( $listString )
	IFS="$oldIFS"

	# 获取当前p12number
	p12Number=`openssl pkcs12 -password pass:"${p12pwd}"  -in  "$p12path" -nodes 2>&1|grep "(*)"|sed "s/.*(//g"|sed "s/).*//g"|head -n 1`
	echo $p12Number


	for i in "${!list[@]}"; do
		echo "${list[$i]}"
		temp=`echo ${list[$i]}|sed 's/.*(//g'|sed 's/)"//g'`
		echo $temp
		if [[ $temp == $p12Number ]]; then
			# 获取正确的证书名称
			CODE_SIGN_IDENTITY=`echo ${list[$i]}|sed 's/.* "//g'|sed 's/)"/)/g'`
			echo $CODE_SIGN_IDENTITY
		fi
	done

	echo "------------- loadCertificate success -------------"
}

# p12证书 相关------------------------------------------------------------------------
function loadProvisionUUID() {
	provisitionUUID=`/usr/libexec/PlistBuddy -c 'Print :UUID' /dev/stdin <<< $(security cms -D -i ${PROVISIONING_FILE_PATH})`
	cp ${PROVISIONING_FILE_PATH} ~/Library/MobileDevice/Provisioning\ Profiles/${provisitionUUID}.mobileprovision
}

# 编译 相关------------------------------------------------------------------------

function buildTarget() {
	# 编译  #-derivedDataPath 在项目目录下生成build文件夹
	if [[ -d ./build ]]; then
		#statements
		rm -r ./build
	fi

	hasWorkspace="0"
	for path in `ls`; do
		#statements
		subfix=`echo ${path}|grep "\.xcworkspace"`
		echo $subfix
		if [[ $subfix != "" ]]; then
			hasWorkspace="1"
			break
		fi
	done
	
	if [[ $hasWorkspace == "1" ]]
		then
			# 编译workspace
			xcodebuild -workspace $targetname.xcworkspace -scheme ${targetname} CODE_SIGN_IDENTITY="${CODE_SIGN_IDENTITY}" PROVISIONING_PROFILE_${targetname}="${provisitionUUID}" -configuration "${CONFIGURATION}" -derivedDataPath ./build clean build
		else
			# 编译project 
			xcodebuild -project $targetname.xcodeproj -scheme ${targetname} CODE_SIGN_IDENTITY="${CODE_SIGN_IDENTITY}" PROVISIONING_PROFILE_${targetname}="${provisitionUUID}" -configuration "${CONFIGURATION}" -derivedDataPath ./build clean build
	fi
	
}

function packageApp() {
	now=`date "+%Y-%m-%d_%H_%M_%S"`
	appName="${targetname}_${CONFIGURATION}_${now}"
	xcrun -sdk iphoneos packageapplication -v "build/Build/Products/${CONFIGURATION}-iphoneos/${targetname}.app" -o "`pwd`/build/${appName}.ipa"
}

function chackHasPodfile() {
	cd $targetpath
	for path in `ls`; do
		temp=`echo $path|grep 'Podfile'`
		if [[ ${#temp} == 7 ]]; then
			#statements
			hascocoapods="1"
			break
		fi
	done
}

function generatePlist() {
	xmlString="<?xml version=\"1.0\" encoding=\"UTF-8\"?>
				<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">
				<plist version=\"1.0\">
				<dict>
				    <key>items</key>
				    <array>
				        <dict>
				            <key>assets</key>
				            <array>
				                <dict>
				                    <key>kind</key>
				                    <string>software-package</string>
				                    <key>url</key>
				                    <string>${dowloadBaseUrl}/${appName}.ipa</string>
				                </dict>
				            </array>
				        <key>metadata</key>
				            <dict>
				                <key>bundle-identifier</key>
				                <string>${bundleId}</string>
				                <key>bundle-version</key>
				                <string>1.1.0</string>
				                <key>kind</key>
				                <string>software</string>
				                <key>releaseNotes</key>
				                <string>1.1版本发布</string>
				                <key>title</key>
				                <string>${targetname}</string>
				            </dict>
				        </dict>
				    </array>
				</dict>
				</plist>"
	echo $xmlString > ${plistExportPath}/${appName}.plist
}

function getBundleId() {
	cd $targetpath
	cd $targetpath/$targetname.xcodeproj
	string=`cat project.pbxproj|grep "PRODUCT_BUNDLE_IDENTIFIER"|sed 1,2d|sed "s/;.*//g"|sed "s/.*= //g"`
	echo $string
	bundleId=$string
}

# 主函数------------------------------------------------------------------------

function main() {
	cd ${targetpath}

	if [ ! -d ".svn" ]; then
	  	checkoutFromSVN
	  	chackHasPodfile
	  	if [[ $hascocoapods == "1" ]]; then
	  		installPod
	  		open ${targetname}.xcworkspace # 第一次需要打开，后面才能找到scheme
	  	fi
	 	# 暂停一下
		ping -c 10 127.0.0.1 >/dev/null

	 else
	 	updateSVN
	 	chackHasPodfile
	 	if [[ $hascocoapods == "1" ]]; then
	  		updatePod
	  	fi
	 	
	fi


	loadCertificate
	loadProvisionUUID
	buildTarget
	packageApp

	# copy ipa 到目标位置
	if [[ ${#ipaExportPath} != 0 ]]; then
		#statements
		cp "build/${appName}.ipa" "${ipaExportPath}"
	fi
	getBundleId
	generatePlist
}

main


