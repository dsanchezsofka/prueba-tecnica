FROM  adoptopenjdk:11-jre-openj9
EXPOSE 80
WORKDIR /app
COPY build/libs/applications-app-service-*.jar /app/app.jar
CMD java -XX:MaxRAMPercentage=90 \
         -XX:OnOutOfMemoryError="kill -9 %p" \
         -jar /app/app.jar