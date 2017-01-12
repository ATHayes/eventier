package com.athayes.eventier;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private List<Event> ITEMS;
    private ArrayList<FacebookPage> facebookPages;

    private Map<String, Event> ITEM_MAP;
    String myFormat = "dd/MM/yy"; // from strings

    private GlobalVariables() {
        ITEMS = new ArrayList<Event>();
        ITEM_MAP = new HashMap<String, Event>();

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


        addToITEMS(createEvent(0, "Silicon Valley Screening", "Watch two episodes of the award winning comedy series based " +
                        "around a new tech startup as they make their way into the world of business in Silicon Valley.",
                "Netsoc", "WGB G03", "7pm", sdf.format(today)));
        addToITEMS(createEvent(1, "E&S Launch Night", "Learn about what E&S does.", "E&S", "Boole 3", "7pm", sdf.format(today)));
        addToITEMS(createEvent(2, "The Politics of Technology", "Featuring TD Sean Sherlock.", "Government and Politics Society", "Boole 1", "7pm", sdf.format(tomorrow)));
        addToITEMS(createEvent(3, "Treehouse of Horror quiz", "Spooky quiz", "SimpSoc", "Boole 4", "8pm", sdf.format(dayAfterTomorrow)));
        addToITEMS(createEvent(4, "US Presidential Elections Screening",
                "It's the time we've all been waiting for. The finale of what has proved to be an election of moments of" +
                        " wonder and moments of \"oh god\". Watch it with Gov&Pol Society in the SU Common Room kicking off at 10 until late." +
                        " There will be pizza and even cake, maybe even election themed cake !",
                "GovPol", "Boole 1", "7pm", sdf.format(today)));
        addToITEMS(createEvent(5, "UCC International Student Party", "Halloween has come and gone. All the fun is over and you've " +
                        "nothing else to do but study now, right?" +
                        " Wrong. Because this Friday night, UCC International Students Society are organising a" +
                        " party at the Old Oak. Come one, come all because as always it...",
                "UCC International Student Society", "Boole 4", "8pm", sdf.format(today)));
        addToITEMS(createEvent(6, "Food Crawl 2016",
                "Here we have it! The most anticipated Foodies event of the year! We will be holding the 2016 Food Crawl on Wednesday the 16th of November. " +
                        "For those of you who may be unfamiliar with how the Food Crawl works, we will be taking you on a journey through the streets of Cork to sample some of the finest cuisines Cork City has to offer! We have a delicious variety of foods for you to sample from some of your favourite Cork restaurants and we'll also be introducing you to some you may not yet be familiar with! "
                , "Foodies", "Boole Library", "7pm", sdf.format(today)));
        addToITEMS(createEvent(7, "COMM CUP Finals",
                "Our Comm Cup finals will be kicking off at 1pm as the 'Renford Rejects' will play head to head against 'Scouting for Boys' for the title of the Women's Comm Cup Champions. The highly anticipated Men’s final of 'Magellas lads 69 vs Lads on Touré/Tweets as bad as Niall Anglin' will follow at 2pm. Both games promise showcases of professional and fast paced soccer. " +
                        "Be sure to join all of us here at the Commerce Society for the Comm Cup celebrations in Barberella at 8 P.M. ",
                "Comm Soc", "Boole 4", "8pm", sdf.format(today)));


        //TODO error handling for these pages.
        facebookPages = new ArrayList<>();

        facebookPages.add(new FacebookPage("uccscifi", "Sci-Fi Society"));
        facebookPages.add(new FacebookPage("uccphilosoph", "Philosophical Society"));
        facebookPages.add(new FacebookPage("NetsocUCC", "Networking, Gaming and Technology"));
        facebookPages.add(new FacebookPage("Accounting And Finance", "uccaccfin"));
        facebookPages.add(new FacebookPage("Africa", "uccafrica"));
        facebookPages.add(new FacebookPage("Amnesty", "UCCAmnesty"));
        facebookPages.add(new FacebookPage("An Chuallact", "anchuallachtucc"));
        facebookPages.add(new FacebookPage("Animal Welfare", "AniWelUCC"));
        facebookPages.add(new FacebookPage("Archaeological", "UCCArchSoc"));
        facebookPages.add(new FacebookPage("An Chuallact", "anchuallachtucc"));
        facebookPages.add(new FacebookPage("Art", "uccart.soc"));
        facebookPages.add(new FacebookPage("Barnardos", "UCCBarnardos"));
        facebookPages.add(new FacebookPage("Biology", "uccbiosoc"));
        facebookPages.add(new FacebookPage("BIS Society", "BisSociety"));
        facebookPages.add(new FacebookPage("Cancer", "ucc.cancersociety"));
        facebookPages.add(new FacebookPage("CCAE Architecture", "ccaearchitecturesociety"));
        facebookPages.add(new FacebookPage("Chemical", "236000773224765"));
        facebookPages.add(new FacebookPage("Chinese", "UccChineseSociety"));
        facebookPages.add(new FacebookPage("Choral", "UCCChoral"));
        facebookPages.add(new FacebookPage("Christian Union", "UCCChristianUnionSociety"));
        facebookPages.add(new FacebookPage("Clinical Therapies", "UccClinicalTherapiesSociety"));
        facebookPages.add(new FacebookPage("Comedy", "ucccomedy"));
        facebookPages.add(new FacebookPage("Commerce", "commsoc.ucc"));
        facebookPages.add(new FacebookPage("Dentist", "dentsocucc"));
        facebookPages.add(new FacebookPage("Disability Activism & Awareness", "uccdaasoc"));
        facebookPages.add(new FacebookPage("DJ", "djsocucc"));
        facebookPages.add(new FacebookPage("Dramat", "UCCDramat"));
        facebookPages.add(new FacebookPage("Economics", "ucceconomics"));
        facebookPages.add(new FacebookPage("Engineering", "UCCEngSoc"));
        facebookPages.add(new FacebookPage("Engineers Without Borders", "EngineerswithoutbordersUCC"));
        facebookPages.add(new FacebookPage("English", "1528413287397046"));
        facebookPages.add(new FacebookPage("Environment", "UCCEnvirosoc"));
        facebookPages.add(new FacebookPage("Europa", "ucceuropa"));
        facebookPages.add(new FacebookPage("Fashion", "155068187434"));
        facebookPages.add(new FacebookPage("Feminist", "UCCFemSoc"));
        facebookPages.add(new FacebookPage("Gaisce", "GaisceUCC"));
        facebookPages.add(new FacebookPage("Friends Of MSF", "uccfomsf"));
        facebookPages.add(new FacebookPage("Fine Gael", "uccyoungfinegael"));
        facebookPages.add(new FacebookPage("FLAC", "293165574222535"));
        facebookPages.add(new FacebookPage("Film", "uccfilm"));
        facebookPages.add(new FacebookPage("Fianna Fail", "UccFFSoc"));
        facebookPages.add(new FacebookPage("Foodies", "373486662727603"));
        facebookPages.add(new FacebookPage("French", "uccfrenchsoc"));
        facebookPages.add(new FacebookPage("Genetics", "uccgensoc"));
        facebookPages.add(new FacebookPage("Geological", "UccGeological"));
        facebookPages.add(new FacebookPage("German", "uccgermansociety"));
        facebookPages.add(new FacebookPage("Government And Politics", "216465755054445"));
        facebookPages.add(new FacebookPage("Greens", "ucc.greens"));
        facebookPages.add(new FacebookPage("Harry Potter", "HPSocietyUCC"));
//        facebookPages.add(new FacebookPage("Health", "466162393565838"));
//        facebookPages.add(new FacebookPage("Hispanic", "142908783322"));
//        facebookPages.add(new FacebookPage("Historical", "276841219006188"));
//        facebookPages.add(new FacebookPage("Hope Foundation", "UccHopeFoundation"));
//        facebookPages.add(new FacebookPage("Horse Racing", "281964401864450"));
//        facebookPages.add(new FacebookPage("Hot Beverages", "UCCHotBevs"));
//        facebookPages.add(new FacebookPage("Indian", "ucc.indians"));
//        facebookPages.add(new FacebookPage("International Development", "UCCIntDevSoc"));
//        facebookPages.add(new FacebookPage("International Relations", "uccirsoc"));
//        facebookPages.add(new FacebookPage("International Students", "ucc.internationals"));
//        facebookPages.add(new FacebookPage("Islamic", "UCCIslamicSociety"));
//        facebookPages.add(new FacebookPage("Italian", "UCCItalianSociety"));
//        facebookPages.add(new FacebookPage("Japanese", "uccjapansoc"));
//        facebookPages.add(new FacebookPage("Journalism", "UCCJournalismSociety"));
//        facebookPages.add(new FacebookPage("Knitting", "UCCKnitSoc"));
//        facebookPages.add(new FacebookPage("Korean", "ucckoreansociety"));
//        facebookPages.add(new FacebookPage("Labour", "ucclabour"));
//        facebookPages.add(new FacebookPage("Law", "ucclawsociety"));
//        facebookPages.add(new FacebookPage("LGBT", "ucclgbt"));
//        facebookPages.add(new FacebookPage("Macra Na Feirme", "uccmacra"));
//        facebookPages.add(new FacebookPage("Management And Marketing", "MnMSocUCC"));
//        facebookPages.add(new FacebookPage("Math", "uccmathsoc"));
//        facebookPages.add(new FacebookPage("Mature Students", "UCCMSS"));
//        facebookPages.add(new FacebookPage("Medical", "ucc.medsoc"));
//        facebookPages.add(new FacebookPage("Medieval And Renaissance", "UCCMedRen"));
//        facebookPages.add(new FacebookPage("Musical", "uccmusicalsociety"));
//        facebookPages.add(new FacebookPage("Mythology", "UccMythology"));
//        facebookPages.add(new FacebookPage("Networking Gaming and Technology", "UCCNetsoc"));
//        facebookPages.add(new FacebookPage("Nursing And Midwifery", "UCCnursmidsoc"));
//        facebookPages.add(new FacebookPage("Orchestra", "OrchestraUCC"));
//        facebookPages.add(new FacebookPage("Pharmacy", "pharmsoc.ucc"));
//        facebookPages.add(new FacebookPage("Philosophical", "uccphilosoph"));
//        facebookPages.add(new FacebookPage("Photography", "uccphoto"));
//        facebookPages.add(new FacebookPage("Physics And Astronomy", "uccphysoc"));
//        facebookPages.add(new FacebookPage("Planning", "uccplanning"));
//        facebookPages.add(new FacebookPage("Poker", "uccpokersociety"));
//        facebookPages.add(new FacebookPage("Politics", "PolSocUCC"));
//        facebookPages.add(new FacebookPage("Psychology", "110003839018417"));
//        facebookPages.add(new FacebookPage("SAMH", "uccsamhsoc"));
//        facebookPages.add(new FacebookPage("Science", "uccsciencesociety"));
//        facebookPages.add(new FacebookPage("Scifi", "uccscifi"));
//        facebookPages.add(new FacebookPage("Simon", "Uccsimonsociety"));
//        facebookPages.add(new FacebookPage("Sinn Fein", "uccsinnfein"));
//        facebookPages.add(new FacebookPage("Slainte", "184958275298"));
//        facebookPages.add(new FacebookPage("Social Science", "uccsocsci"));
//        facebookPages.add(new FacebookPage("Socialist", "uccsocialistyouth"));
//        facebookPages.add(new FacebookPage("Sophia", "uccsophia"));
//        facebookPages.add(new FacebookPage("South East Asia", "526366970861527"));
//        facebookPages.add(new FacebookPage("SSDP", "UCCSSDP"));
//        facebookPages.add(new FacebookPage("St. Vincent De Paul", "SVPUCC"));
//        facebookPages.add(new FacebookPage("Suas", "uccsuas"));
//        facebookPages.add(new FacebookPage("Surgeon Noonan", "1650501751859811"));
//        facebookPages.add(new FacebookPage("Surgeon", "surgsoc"));
//        facebookPages.add(new FacebookPage("Traditional Music", "ucctradsoc"));
//        facebookPages.add(new FacebookPage("WARP", "ucc.warps"));

    }

    private static GlobalVariables instance;

    public static GlobalVariables getInstance() {
        if (instance == null) instance = new GlobalVariables();
        return instance;
    }

    /**
     * @param position
     * @param eventName
     * @param pitch
     * @param host
     * @param location
     * @param time
     * @return
     */
    public Event createEvent(int position, String eventName, String pitch, String host, String location, String time, String date) {
        return new Event(String.valueOf(position), eventName, pitch, host, location, time, date);
    }

    /**
     * Method to add items
     * This adds items to both the event LIST and event HASHMAP
     * The event list is used for displaying events on the EventListActivity
     * The event hashmap is used to find an event, given its id - used to display content in the EventDetailActivity
     *
     * @param event
     */
    public void addToITEMS(Event event) {
        ITEMS.add(event);
        ITEM_MAP.put(event.id, event);
    }

    public List<Event> getITEMS() {
        return ITEMS;
    }

    public void setITEMS(List<Event> ITEMS) {
        this.ITEMS = ITEMS;
    }

    /**
     * A map of sample (dummy) items, by ID.
     */
    public Map<String, Event> getITEM_MAP() {
        return ITEM_MAP;
    }

    public void setITEM_MAP(Map<String, Event> ITEM_MAP) {
        this.ITEM_MAP = ITEM_MAP;
    }


    public ArrayList<FacebookPage> getFacebookPages() {
        return facebookPages;
    }
}
