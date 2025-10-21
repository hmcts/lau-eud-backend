 # renovate: datasource=github-releases depName=microsoft/ApplicationInsights-Java
ARG APP_INSIGHTS_AGENT_VERSION=3.7.5
FROM hmctspublic.azurecr.io/base/java:21-distroless

# Change to non-root privilege
USER hmcts

COPY lib/applicationinsights.json /opt/app/
COPY build/libs/lau-eud-backend.jar /opt/app/

EXPOSE 4553
CMD [ "lau-eud-backend.jar" ]
