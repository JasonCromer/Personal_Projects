package com.dev.cromer.jason.whatshappening.logic;


public class MarkerLikesPostRequestParams {

    String url;
    String voteType;

    public MarkerLikesPostRequestParams(String url, String voteType){
        this.url = url;
        this.voteType = voteType;
    }

    public String getUrl(){
        return this.url;
    }

    public String getVoteType(){
        return this.voteType;
    }
}
