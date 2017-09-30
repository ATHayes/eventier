package com.athayes.eventier;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.athayes.eventier.models.FacebookPage;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

// Facebook API

/**
 * Created by anthonyhayes on 05/11/2016.
 */

public class GlobalVariables extends Application {
    //Facebook API
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the SDK before executing any other operations,
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    private ArrayList<FacebookPage> facebookPages;
    ArrayList<FacebookPage> uccPages;
    ArrayList<FacebookPage> torontoPages;

    String myFormat = "dd/MM/yy"; // from strings

    private GlobalVariables() {

        // We want our sample data to always be set to a combination of today, tomorrow and the next day
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        Date today = new Date();
        Date tomorrow = new Date();
        Date dayAfterTomorrow = new Date();

        //
        Calendar calTomorrow = Calendar.getInstance();
        Calendar calDayAfterTomorrow = Calendar.getInstance();

        // Set up tomorrow's date
        calTomorrow.setTime(tomorrow);
        calTomorrow.add(Calendar.DATE, 1);
        tomorrow = calTomorrow.getTime();

        // Set up the day after tomorrow's date
        calDayAfterTomorrow.setTime(dayAfterTomorrow);
        calDayAfterTomorrow.add(Calendar.DATE, 1);
        dayAfterTomorrow = calDayAfterTomorrow.getTime();

        uccPages = new ArrayList<>();
        facebookPages = new ArrayList<>();
        torontoPages = new ArrayList<>();


        // UCC Societies
        uccPages.add(new FacebookPage("Accounting And Finance Society", "uccaccfin"));
        uccPages.add(new FacebookPage("Africa Society", "uccafrica"));
        uccPages.add(new FacebookPage("Amnesty Society", "UCCAmnesty"));
        uccPages.add(new FacebookPage("An Chuallact", "anchuallachtucc"));
        uccPages.add(new FacebookPage("Animal Welfare Society", "AniWelUCC"));
        uccPages.add(new FacebookPage("Archaeological Society", "UCCArchSoc"));
        uccPages.add(new FacebookPage("Art Society", "uccart.soc"));
        uccPages.add(new FacebookPage("Barnardos Society", "UCCBarnardos"));
        uccPages.add(new FacebookPage("Biology Society", "uccbiosoc"));
        uccPages.add(new FacebookPage("BIS Society", "BisSociety"));
        uccPages.add(new FacebookPage("Cancer Society", "ucc.cancersociety"));
        uccPages.add(new FacebookPage("CCAE Architecture Society", "ccaearchitecturesociety"));
        uccPages.add(new FacebookPage("Chemical Society", "236000773224765"));
        uccPages.add(new FacebookPage("Chinese Society", "UccChineseSociety"));
        uccPages.add(new FacebookPage("Choral Society", "UCCChoral"));
        uccPages.add(new FacebookPage("Christian Union Society", "UCCChristianUnionSociety"));
        uccPages.add(new FacebookPage("Clinical Therapies Society", "UccClinicalTherapiesSociety"));
        uccPages.add(new FacebookPage("Comedy Society", "ucccomedy"));
        uccPages.add(new FacebookPage("Commerce Society", "commsoc.ucc"));
        uccPages.add(new FacebookPage("Dentist Society", "dentsocucc"));
        uccPages.add(new FacebookPage("Disability Activism & Awareness Society", "uccdaasoc"));
        uccPages.add(new FacebookPage("DJ Society", "djsocucc"));
        uccPages.add(new FacebookPage("Dramat", "UCCDramat"));
        uccPages.add(new FacebookPage("Economics Society", "ucceconomics"));
        uccPages.add(new FacebookPage("Engineering Society", "UCCEngSoc"));
        uccPages.add(new FacebookPage("Engineers Without Borders Society", "EngineerswithoutbordersUCC"));
        uccPages.add(new FacebookPage("English Society", "1528413287397046"));
        uccPages.add(new FacebookPage("Entrepreneurial and Social Society", "261747967180908"));
        uccPages.add(new FacebookPage("Environment Society", "UCCEnvirosoc"));
        uccPages.add(new FacebookPage("Europa Society", "ucceuropa"));
        uccPages.add(new FacebookPage("Fashion Society", "155068187434"));
        uccPages.add(new FacebookPage("Feminist Society", "UCCFemSoc"));
        uccPages.add(new FacebookPage("Gaisce Society", "GaisceUCC"));
        uccPages.add(new FacebookPage("Friends Of MSF Society", "uccfomsf"));
        uccPages.add(new FacebookPage("Fine Gael Society", "uccyoungfinegael"));
        uccPages.add(new FacebookPage("FLAC Society", "293165574222535"));
        uccPages.add(new FacebookPage("Film Society", "uccfilm"));
        uccPages.add(new FacebookPage("Fianna Fail Society", "UccFFSoc"));
        uccPages.add(new FacebookPage("Foodies", "373486662727603"));
        uccPages.add(new FacebookPage("French Society", "uccfrenchsoc"));
        uccPages.add(new FacebookPage("Genetics Society", "uccgensoc"));
        uccPages.add(new FacebookPage("Geological Society", "UccGeological"));
        uccPages.add(new FacebookPage("German Society", "uccgermansociety"));
        uccPages.add(new FacebookPage("Government And Politics Society", "216465755054445"));
        uccPages.add(new FacebookPage("Greens Society", "ucc.greens"));
        uccPages.add(new FacebookPage("Harry Potter Society", "HPSocietyUCC"));
        uccPages.add(new FacebookPage("Health Society", "466162393565838"));
        uccPages.add(new FacebookPage("Hispanic Society", "142908783322"));
        uccPages.add(new FacebookPage("Historical Society", "276841219006188"));
        uccPages.add(new FacebookPage("Hope Foundation Society", "UccHopeFoundation"));
        uccPages.add(new FacebookPage("Horse Racing Society", "281964401864450"));
        uccPages.add(new FacebookPage("Hot Beverages Society", "UCCHotBevs"));
        uccPages.add(new FacebookPage("Indian Society", "ucc.indians"));
        uccPages.add(new FacebookPage("International Development Society", "UCCIntDevSoc"));
        uccPages.add(new FacebookPage("International Relations Society", "uccirsoc"));
        uccPages.add(new FacebookPage("International Students Society", "ucc.internationals"));
        uccPages.add(new FacebookPage("Islamic Society", "UCCIslamicSociety"));
        uccPages.add(new FacebookPage("Italian Society", "UCCItalianSociety"));
        uccPages.add(new FacebookPage("Japanese Society", "uccjapansoc"));
        uccPages.add(new FacebookPage("Journalism Society", "UCCJournalismSociety"));
        uccPages.add(new FacebookPage("Knitting Society", "UCCKnitSoc"));
        uccPages.add(new FacebookPage("Korean Society", "ucckoreansociety"));
        uccPages.add(new FacebookPage("Labour Society", "ucclabour"));
        uccPages.add(new FacebookPage("Law Society", "ucclawsociety"));
        uccPages.add(new FacebookPage("LGBT Society", "ucclgbt"));
        uccPages.add(new FacebookPage("Macra Na Feirme", "uccmacra"));
        uccPages.add(new FacebookPage("Management And Marketing Society", "MnMSocUCC"));
        uccPages.add(new FacebookPage("Math Society", "uccmathsoc"));
        uccPages.add(new FacebookPage("Mature Students Society", "UCCMSS"));
        uccPages.add(new FacebookPage("Medical Society", "ucc.medsoc"));
        uccPages.add(new FacebookPage("Medieval And Renaissance Society", "UCCMedRen"));
        uccPages.add(new FacebookPage("Musical Society", "uccmusicalsociety"));
        uccPages.add(new FacebookPage("Mythology Society", "UccMythology"));
        uccPages.add(new FacebookPage("Networking Gaming and Technology Society", "UCCNetsoc"));
        uccPages.add(new FacebookPage("Nursing And Midwifery Society", "UCCnursmidsoc"));
        uccPages.add(new FacebookPage("Orchestra Society", "OrchestraUCC"));
        uccPages.add(new FacebookPage("Pharmacy Society", "pharmsoc.ucc"));
        uccPages.add(new FacebookPage("Philosophical Society", "uccphilosoph"));
        uccPages.add(new FacebookPage("Photography Society", "uccphoto"));
        uccPages.add(new FacebookPage("Physics And Astronomy Society", "uccphysoc"));
        uccPages.add(new FacebookPage("Planning Society", "uccplanning"));
        uccPages.add(new FacebookPage("Poker Society", "uccpokersociety"));
        uccPages.add(new FacebookPage("Politics Society", "PolSocUCC"));
        uccPages.add(new FacebookPage("Psychology Society", "110003839018417"));
        uccPages.add(new FacebookPage("SAMH Society", "uccsamhsoc"));
        uccPages.add(new FacebookPage("Science Society", "uccsciencesociety"));
        uccPages.add(new FacebookPage("Scifi Society", "uccscifi"));
        uccPages.add(new FacebookPage("Simon Society", "Uccsimonsociety"));
        uccPages.add(new FacebookPage("Sinn Fein Society", "uccsinnfein"));
        uccPages.add(new FacebookPage("Slainte Society", "184958275298"));
        uccPages.add(new FacebookPage("Social Science Society", "uccsocsci"));
        uccPages.add(new FacebookPage("Socialist Society", "uccsocialistyouth"));
        uccPages.add(new FacebookPage("Sophia Society", "uccsophia"));
        uccPages.add(new FacebookPage("South East Asia Society", "526366970861527"));
        uccPages.add(new FacebookPage("SSDP Society", "UCCSSDP"));
        uccPages.add(new FacebookPage("St. Vincent De Paul Society", "SVPUCC"));
        uccPages.add(new FacebookPage("Suas Society", "uccsuas"));
        uccPages.add(new FacebookPage("Surgeon Noonan Society", "1650501751859811"));
        uccPages.add(new FacebookPage("Surgeon Society", "surgsoc"));
        uccPages.add(new FacebookPage("Traditional Music Society", "ucctradsoc"));
        uccPages.add(new FacebookPage("WARPS", "ucc.warps"));

        // Pubs / Clubs
        uccPages.add(new FacebookPage("Franciscan Well Brew Pub", "121375577958143"));
        uccPages.add(new FacebookPage("Electric", "ElectricCork"));
        uccPages.add(new FacebookPage("Sober Lane", "91646667265"));
        uccPages.add(new FacebookPage("Crane Lane", "cranelane"));
        uccPages.add(new FacebookPage("The Bodega", "BodegaCork"));
        uccPages.add(new FacebookPage("The Pav", "ThePavCork"));
        uccPages.add(new FacebookPage("The Secret Garden, Cork", "TheSecretGardenCork"));

        // Cinema
        uccPages.add(new FacebookPage("Triskel Arts Centre", "148849978463380"));
        uccPages.add(new FacebookPage("Gate Cinema Cork", "GateCinemasCork"));

        // Art
        uccPages.add(new FacebookPage("Crawford Art Gallery", "192247684119097"));
        uccPages.add(new FacebookPage("The Glucksman", "51529118892"));

        // Gaming
        uccPages.add(new FacebookPage("Cork Fighting Game Community", "243557759182283"));

        // Academic
        uccPages.add(new FacebookPage("UCC College of Business and Law", "115489451872851"));

        // Music
        uccPages.add(new FacebookPage("Live at St. Luke's", "538711329601290"));

        // Theatre
        uccPages.add(new FacebookPage("Everyman Theatre", "80651236688"));
        uccPages.add(new FacebookPage("Granary Theatre", "175256179251424"));
        uccPages.add(new FacebookPage("Cork Opera House", "357268343449"));

        // Literature
        uccPages.add(new FacebookPage("Waterstone's Bookstore, Cork", "corkwaterstones"));



        // -- Toronto Pages --
        torontoPages.add(new FacebookPage("Toronto Archery", "torontoarchery"));
        torontoPages.add(new FacebookPage("Things to do in Toronto", "659751204108400"));

        // Sport
        torontoPages.add(new FacebookPage("Toronto Dodgeball", "170383002982639"));

        // Drinking
        torontoPages.add(new FacebookPage("The Porch Toronto", "ThePorchToronto"));
        torontoPages.add(new FacebookPage("Toronto Ocktoberfest", "TorontoOktoberfest"));

        // Music
        torontoPages.add(new FacebookPage("Niteowl Toronto", "nightowltoronto"));
        torontoPages.add(new FacebookPage("The Hideout Torotno", ""));

        // Film
        torontoPages.add(new FacebookPage("Toronto Film School", "TorontoFilmSchool"));
        torontoPages.add(new FacebookPage("The Royal Cinema - Toronto", "TheRoyalCinema"));

    }

    private static GlobalVariables instance;
    public static GlobalVariables getInstance() {
        if (instance == null) instance = new GlobalVariables();
        return instance;
    }

    public ArrayList<FacebookPage> getFacebookPages() {
        return facebookPages;
    }

    public FacebookPage getFacebookPage(String facebookID) {
        for (FacebookPage page : facebookPages) {
            if (page.getFacebookID().compareTo(facebookID) == 0) {
                return page;
            }
        }
        return null;
    }

    //TODO I think this needs to be adapted to singelton

    //todo Associate each object with a string, and just set the fbpages to that string? There has to be a cleaner way to do this...
    // key/value pair list and string key
    public void setPageCollection(String pageCollection) {
        switch (pageCollection){
            case "Toronto":
                facebookPages.clear();
                facebookPages.addAll(torontoPages);
                break;
            case "Cork (UCC)":
                facebookPages.clear();
                facebookPages.addAll(uccPages);
                break;
            default:
                facebookPages.clear();
                facebookPages.addAll(uccPages);
                break;
        }
    }
}
