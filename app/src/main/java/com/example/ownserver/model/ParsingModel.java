package com.example.ownserver.model;

public class ParsingModel {
    private String systemName;
    private String buildDate;
    private String serverApi;
    private String phpApi;

    public ParsingModel(String systemName, String buildDate, String serverApi, String phpApi) {
        this.systemName = systemName;
        this.buildDate = buildDate;
        this.serverApi = serverApi;
        this.phpApi = phpApi;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }

    public String getServerApi() {
        return serverApi;
    }

    public void setServerApi(String serverApi) {
        this.serverApi = serverApi;
    }

    public String getPhpApi() {
        return phpApi;
    }

    public void setPhpApi(String phpApi) {
        this.phpApi = phpApi;
    }
}
