

helpfunc(){
  echo "usage: sh build-rpm.sh --mappingsFilePath xxx"
}

# shellcheck disable=SC2006
GETOPT_ARGS=`getopt -o l:r:b:w: -al mappingsFilePath: -- "$@"`

eval set -- "$GETOPT_ARGS"
while [ -n "$1" ]
do
  case "$1" in
   -m|--mappingsFilePath) mappingsFilePath=$2; shift 2;;
   --) break;;
   *) helpfunc; break;;
  esac
done

if [[ -z $mappingsFilePath ]]; then
  helpfunc
  exit
fi

java -cp ./lib/rpm-package-1.0.jar:./lib/* com.zh.ch.bigdata.rpm.build.BuildRPM --mappingsFilePath "${mappingsFilePath}"

rpmName=$(cat ${mappingsFilePath} | grep rpmName | sed s/[[:space:]]//g | sed 's/\"//g')
rpmVersion=$(cat ${mappingsFilePath} | grep rpmVersion | sed s/[[:space:]]//g | sed 's/\"//g')
targetRpmPath=$(cat ${mappingsFilePath} | grep targetRpmPath | sed s/[[:space:]]//g | sed 's/\"//g')
temp_name=${rpmName//,/}
temp_version=${rpmVersion//,/}
temp_rpmPath=${targetRpmPath//,/}

OLD_IFS="$IFS"
IFS=":"
name=($temp_name)
version=($temp_version)
rpm_path=($temp_rpmPath)
IFS="$OLD_IFS"
target=${rpm_path[1]}/${name[1]}-${version[1]}
cd ${target} && mvn clean rpm:rpm
