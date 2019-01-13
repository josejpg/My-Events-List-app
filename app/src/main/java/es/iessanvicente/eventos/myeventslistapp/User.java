package es.iessanvicente.eventos.myeventslistapp;

public class User {

    /**
     * Variables
     */
    private int id;
    private String avatar;
    private String name;
    private String email;
    private String psw;
    private String phone;

    /**
     * Return the User ID
     * @return User ID
     */
    public int getID(){ return this.id; }

    /**
     * Add a ID to User
     * @param id
     */
    public void setID( int id ){ this.id = id; }

    /**
     * Return the User's avatar
     * @return avatar
     */
    public String getAvatar(){ return this.avatar; }

    /**
     * Add a avatar to User
     * @param avatar
     */
    public void setAvatar( String avatar ){ this.avatar = avatar; }

    /**
     * Return the User's name
     * @return name
     */
    public String getName(){ return this.name; }

    /**
     * Add a name to User
     * @param name
     */
    public void setName( String name ){ this.name = name; }

    /**
     * Return the User's email
     * @return email
     */
    public String getEmail(){ return this.email; }

    /**
     * Add a email to User
     * @param email
     */
    public void setEmail( String email ){ this.email = email; }

    /**
     * Return the User's psw
     * @return psw
     */
    public String getPsw(){ return this.psw; }

    /**
     * Add a psw to User
     * @param psw
     */
    public void setPsw( String psw ){ this.psw = psw; }

    /**
     * Return the User's phone
     * @return phone
     */
    public String getPhone(){ return this.phone; }

    /**
     * Add a phone to User
     * @param phone
     */
    public void setPhone( String phone ){ this.phone = phone; }

    /**
     * Empty constructor
     */
    public User(){
        this.id = 0;
        this.avatar = null;
        this.name = null;
        this.email = null;
        this.psw = null;
        this.phone = null;
    }

    /**
     * Constructor with data
     * @param id
     * @param avatar
     * @param name
     * @param email
     * @param psw
     * @param phone
     */
    public User(
            int id,
            String avatar,
            String email,
            String psw,
            String name,
            String phone
    ){
        this.id = id;
        this.avatar = avatar;
        this.name = name;
        this.email = email;
        this.psw = psw;
        this.phone = phone;
    }

    @Override
    public String toString(){
        String result = "";
        if( this.id > 0 ){
            result += "ID:" + this.id + "\n";
        }
        if( this.name != null && !this.name.equals( "" ) ){
            result += "Name:" + this.name + "\n";
        }
        if( this.email != null && !this.email.equals( "" ) ){
            result += "Email:" + this.email + "\n";
        }
        if( this.phone != null && !this.phone.equals( "" ) ){
            result += "Phone:" + this.phone + "\n";
        }
        return result;
    }
}
