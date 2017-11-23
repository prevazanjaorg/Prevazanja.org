package nameplaceholder.prevazanjaorg;

import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Stack;
import java.util.Vector;

/**
 * Created by Jaka on 03/11/2017.
 */

public class PB {
    Connection connection = null;
    String url = "";
    String username = "";
    String password = "";
    final String IP = "89.142.135.17";
    final String DBname = "Prevozi";
    Statement st;
    ResultSet rs;
    String stanje;
    String rezervacija;

    public PB(){
        try {
            Log.e("PB:CON>>", "CONNECTION SUCCESSFUL");
        }catch (Exception e){
            Log.e("PB:CON>>", "CONNECTION FAILED");
        }
    }


    boolean FormResponse(UserData curr){
        if(curr.tip == UserData.STANJE){
            if(GetStanje(curr)){
                Log.e("PB-RES", "STANJE GOOD");
                return true;
            }
            else{
                Log.e("PB-RES", "STANJE BAD");
                return true;
            }
        }
        else if(curr.tip == UserData.REZERVACIJA){
            if(RezervirajSedez(curr)){
                Log.e("PB-RES", "REZERVACIJA GOOD");
                return true;
            }
            else{
                Log.e("PB-RES", "REZERVACIJA BAD");
                return true;
            }
        }
        else if(curr.tip == UserData.PREKLIC){
            PrekliciRezervacijo(curr);
            return true;
        }
        Log.e("PB-SCANNER:>> ", "empty");
        return false;
    }

    boolean GetStanje(UserData curr){
        try{
            //st = connection.createStatement();
            //st.executeUpdate(metoda ki vnre prevoze ponudnika);
            stanje = "Stanje prevoza: " + "\n" + " Na voljo sta dva prevoza:" + "\n" + " 1. IDX001 - Koper-Špar 04:20 - 20:40" + "\n" + " 2. IDX002 - Špar-Stanovanje 16:20 - 20:16";
            //stanje = "Test";
            curr.response = stanje;
            return true;
        }catch(Exception e){ //Exception -> SQLException
            stanje = "Napaka pri dostopu do podatkovne baze";
            Log.e("PB:CONN>>", e.getMessage());
            return false;
        }
    }

    boolean RezervirajSedez(UserData curr){
        try{
            //st = connection.createStatement();
            rezervacija = "Sedež uspešno rezerviran! \nPrevozID: " + curr.prevozID;
            curr.response = rezervacija;
            //executeStatement(sql);
            return true;
        }catch(Exception e){ //Exception -> SQLException
            rezervacija = "Rezervacija ni uspela poskusite kasneje";
            Log.e("PB:CONN>>", e.getMessage());
            return false;
        }
    }

    boolean PrekliciRezervacijo(UserData curr){
        try{
            //st = connection.createStatement();
            //st.executeUpdate(metoda ki preklice rezervacijo);
            curr.response = "Rezervacija prevoza preklicana prevozID: " + curr.prevozID;
            return true;
        }catch(Exception e){ //Exception -> SQLException
            rezervacija = "Preklic prevoza ni uspel poskusite kasneje";
            Log.e("PB:CONN>>", e.getMessage());
            return false;
        }
    }


    Vector<UserData> GetRezervacijeFromPB(){
        Vector<UserData> rezervacije = new Vector<UserData>();
        try{
            //st = connection.createStatement();
           // rs = st.executeQuery(metoda ki vrne rezervacije)
           // while(rs.next()){
            //    UserData a = new UserData(rs.getString("mobitel"),"Imate prevoz!", rs.getString("prevozID"));
            //    rezervacije.add(a);
           // }
            return rezervacije;
        }catch(Exception e){ //Exception -> SQLException
            Log.e("PB:CONN>>", e.getMessage());
            return rezervacije;
        }
    }
}
