rem building for test env, see the result in build.log
rem -e print error message, if clean error, try: -Dmaven.clean.failOnError=false
mvn  clean package -am -e -Dmaven.clean.failOnError=false
