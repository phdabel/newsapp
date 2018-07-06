package abelcorreadias.com.br.newsapp.models;

public class Token extends Model {

    /**
     *  Token variables
     *  OAuth
     */
    public String access_token;
    public long expires_in;
    public String token_type;
    public String scope;
    public String refresh_token;

    public String getBearer(){
        return "Bearer " + this.access_token;
    }

}
