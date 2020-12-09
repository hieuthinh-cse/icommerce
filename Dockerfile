FROM adoptopenjdk/openjdk11:alpine as build
WORKDIR /app
COPY . ./
RUN ./gradlew clean build -x check

FROM adoptopenjdk/openjdk11:alpine
WORKDIR /app
COPY --from=build /app/db db
COPY --from=build /app/elasticsearch ../elasticsearch
COPY --from=build /app/iam/build/libs/iam-1.0.0.jar ./
COPY --from=build /app/outbox/build/libs/outbox-1.0.0.jar ./
COPY --from=build /app/essync/build/libs/essync-1.0.0.jar ./
COPY --from=build /app/icommerce/build/libs/icommerce-1.0.0.jar ./
COPY --from=build /app/dbtool/build/libs/dbtool-1.0.0.jar ./
EXPOSE 9380 9480
CMD ["java", "-jar", "dbtool-1.0.0.jar"]