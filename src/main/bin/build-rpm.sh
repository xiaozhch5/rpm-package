

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

java -cp ../lib/rpm-package-1.0-SNAPSHOT.jar:../lib/* com.zh.ch.bigdata.rpm.build.BuildRPM --mappingsFilePath "${mappingsFilePath}"
