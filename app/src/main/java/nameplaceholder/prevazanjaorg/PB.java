package nameplaceholder.prevazanjaorg;

import android.util.Log;

import java.util.Stack;

/**
 * Created by Jaka on 03/11/2017.
 */

public class PB {
    private String IP = "89.142.135.17";
    private String DB = "Prevozi";
    String stanje;
    String rezervacija;

    public PB(){
        //
    }

    boolean FormResponse(UserData curr){
        if(curr.tip == UserData.STANJE){
            if(GetStanje(curr)){
                Log.e("PB-PB", "STANJE GOOD");
                return true;
            }
            else{
                Log.e("PB-PB", "STANJE BAD");
                return true;
            }
        }
        else if(curr.tip == UserData.REZERVACIJA){
            if(RezervirajSedez(curr)){
                Log.e("PB-PB", "REZERVACIJA GOOD");
                return true;
            }
            else{
                Log.e("PB-PB", "REZERVACIJA BAD");
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
            stanje = "Stanje prevoza: " + "\n" + " Na voljo sta dva prevoza:" + "\n" + " 1. IDX001 - Koper-Špar 04:20 - 20:40" + "\n" + " 2. IDX002 - Špar-Stanovanje 16:20 - 20:16";
            //stanje = "Test";
            curr.response = stanje;
            Log.e("PB-PB:>>","Stanje good" );
            return true;
        }catch(Exception e){
            stanje = "Napaka pri dostopu do podatkovne baze";
            Log.e("PB-PB:>>","Stanje ERROR" );
            return false;
        }
    } // PB

    boolean RezervirajSedez(UserData curr){
        try{
            rezervacija = "Sedež uspešno rezerviran! \nPrevozID: " + curr.prevozID;
            curr.response = rezervacija;
            Log.e("PB-PB:>>","Rezervacija good" );
            return true;
        }catch(Exception e){
            rezervacija = "Rezervacija ni uspela poskusite kasneje";
            Log.e("PB-PB:>>","Rezervacija fail" );
            return false;
        }
    }

    boolean PrekliciRezervacijo(UserData curr){
        curr.response = "Rezervacija prevoza preklicana prevozID: " + curr.prevozID;
        return true;
    }


    Stack<UserData> GetRezervacijeFromPB(){
        Stack<UserData> tmp = new Stack<UserData>();
        //preberi in pushaj iz baze nove rezervacije
        Log.e("PB-PB:>>","Iscem rezervacije..." );
        return tmp;
    }
}
