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


//        addToITEMS(createEvent(0, "Silicon Valley Screening", "Watch two episodes of the award winning comedy series based " +
//                        "around a new tech startup as they make their way into the world of business in Silicon Valley.",
//                "Netsoc", "WGB G03", "7pm", sdf.format(today)));
//        addToITEMS(createEvent(1, "E&S Launch Night", "Learn about what E&S does.", "E&S", "Boole 3", "7pm", sdf.format(today)));
//        addToITEMS(createEvent(2, "The Politics of Technology", "Featuring TD Sean Sherlock.", "Government and Politics Society", "Boole 1", "7pm", sdf.format(tomorrow)));
//        addToITEMS(createEvent(3, "Treehouse of Horror quiz", "Spooky quiz", "SimpSoc", "Boole 4", "8pm", sdf.format(dayAfterTomorrow)));
//        addToITEMS(createEvent(4, "US Presidential Elections Screening",
//                "It's the time we've all been waiting for. The finale of what has proved to be an election of moments of" +
//                        " wonder and moments of \"oh god\". Watch it with Gov&Pol Society in the SU Common Room kicking off at 10 until late." +
//                        " There will be pizza and even cake, maybe even election themed cake !",
//                "GovPol", "Boole 1", "7pm", sdf.format(today)));
//        addToITEMS(createEvent(5, "UCC International Student Party", "Halloween has come and gone. All the fun is over and you've " +
//                        "nothing else to do but study now, right?" +
//                        " Wrong. Because this Friday night, UCC International Students Society are organising a" +
//                        " party at the Old Oak. Come one, come all because as always it...",
//                "UCC International Student Society", "Boole 4", "8pm", sdf.format(today)));
//        addToITEMS(createEvent(6, "Food Crawl 2016",
//                "Here we have it! The most anticipated Foodies event of the year! We will be holding the 2016 Food Crawl on Wednesday the 16th of November. " +
//                        "For those of you who may be unfamiliar with how the Food Crawl works, we will be taking you on a journey through the streets of Cork to sample some of the finest cuisines Cork City has to offer! We have a delicious variety of foods for you to sample from some of your favourite Cork restaurants and we'll also be introducing you to some you may not yet be familiar with! "
//                , "Foodies", "Boole Library", "7pm", sdf.format(today)));
//        addToITEMS(createEvent(7, "COMM CUP Finals",
//                "Our Comm Cup finals will be kicking off at 1pm as the 'Renford Rejects' will play head to head against 'Scouting for Boys' for the title of the Women's Comm Cup Champions. The highly anticipated Men’s final of 'Magellas lads 69 vs Lads on Touré/Tweets as bad as Niall Anglin' will follow at 2pm. Both games promise showcases of professional and fast paced soccer. " +
//                        "Be sure to join all of us here at the Commerce Society for the Comm Cup celebrations in Barberella at 8 P.M. ",
//                "Comm Soc", "Boole 4", "8pm", sdf.format(today)));


        //TODO error handling for these pages.
        facebookPages = new ArrayList<>();

        facebookPages.add(new FacebookPage("uccscifi", "Sci-Fi Society"));
        facebookPages.add(new FacebookPage("Accounting And Finance Society", "uccaccfin"));
        facebookPages.add(new FacebookPage("Africa Society", "uccafrica"));
        facebookPages.add(new FacebookPage("Amnesty Society", "UCCAmnesty"));
        facebookPages.add(new FacebookPage("An Chuallact", "anchuallachtucc"));
        facebookPages.add(new FacebookPage("Animal Welfare Society", "AniWelUCC"));
        facebookPages.add(new FacebookPage("Archaeological Society", "UCCArchSoc"));
        facebookPages.add(new FacebookPage("Art Society", "uccart.soc"));
        facebookPages.add(new FacebookPage("Barnardos Society", "UCCBarnardos"));
        facebookPages.add(new FacebookPage("Biology Society", "uccbiosoc"));
        facebookPages.add(new FacebookPage("BIS Society", "BisSociety"));
        facebookPages.add(new FacebookPage("Cancer Society", "ucc.cancersociety"));
        facebookPages.add(new FacebookPage("CCAE Architecture Society", "ccaearchitecturesociety"));
        facebookPages.add(new FacebookPage("Chemical Society", "236000773224765"));
        facebookPages.add(new FacebookPage("Chinese Society", "UccChineseSociety"));
        facebookPages.add(new FacebookPage("Choral Society", "UCCChoral"));
        facebookPages.add(new FacebookPage("Christian Union Society", "UCCChristianUnionSociety"));
        facebookPages.add(new FacebookPage("Clinical Therapies Society", "UccClinicalTherapiesSociety"));
        facebookPages.add(new FacebookPage("Comedy Society", "ucccomedy"));
        facebookPages.add(new FacebookPage("Commerce Society", "commsoc.ucc"));
        facebookPages.add(new FacebookPage("Dentist Society", "dentsocucc"));
        facebookPages.add(new FacebookPage("Disability Activism & Awareness Society", "uccdaasoc"));
        facebookPages.add(new FacebookPage("DJ Society", "djsocucc"));
        facebookPages.add(new FacebookPage("Dramat", "UCCDramat"));
        facebookPages.add(new FacebookPage("Economics Society", "ucceconomics"));
        facebookPages.add(new FacebookPage("Engineering Society", "UCCEngSoc"));
        facebookPages.add(new FacebookPage("Engineers Without Borders Society", "EngineerswithoutbordersUCC"));
        facebookPages.add(new FacebookPage("English Society", "1528413287397046"));
        facebookPages.add(new FacebookPage("Environment Society", "UCCEnvirosoc"));
        facebookPages.add(new FacebookPage("Europa Society", "ucceuropa"));
        facebookPages.add(new FacebookPage("Fashion Society", "155068187434"));
        facebookPages.add(new FacebookPage("Feminist Society", "UCCFemSoc"));
        facebookPages.add(new FacebookPage("Gaisce Society", "GaisceUCC"));
        facebookPages.add(new FacebookPage("Friends Of MSF Society", "uccfomsf"));
        facebookPages.add(new FacebookPage("Fine Gael Society", "uccyoungfinegael"));
        facebookPages.add(new FacebookPage("FLAC Society", "293165574222535"));
        facebookPages.add(new FacebookPage("Film Society", "uccfilm"));
        facebookPages.add(new FacebookPage("Fianna Fail Society", "UccFFSoc"));
        facebookPages.add(new FacebookPage("Foodies", "373486662727603"));
        facebookPages.add(new FacebookPage("French Society", "uccfrenchsoc"));
        facebookPages.add(new FacebookPage("Genetics Society", "uccgensoc"));
        facebookPages.add(new FacebookPage("Geological Society", "UccGeological"));
        facebookPages.add(new FacebookPage("German Society", "uccgermansociety"));
        facebookPages.add(new FacebookPage("Government And Politics Society", "216465755054445"));
        facebookPages.add(new FacebookPage("Greens Society", "ucc.greens"));
        facebookPages.add(new FacebookPage("Harry Potter Society", "HPSocietyUCC"));
        facebookPages.add(new FacebookPage("Health Society", "466162393565838"));
        facebookPages.add(new FacebookPage("Hispanic Society", "142908783322"));
        facebookPages.add(new FacebookPage("Historical Society", "276841219006188"));
        facebookPages.add(new FacebookPage("Hope Foundation Society", "UccHopeFoundation"));
        facebookPages.add(new FacebookPage("Horse Racing Society", "281964401864450"));
        facebookPages.add(new FacebookPage("Hot Beverages Society", "UCCHotBevs"));
        facebookPages.add(new FacebookPage("Indian Society", "ucc.indians"));
        facebookPages.add(new FacebookPage("International Development Society", "UCCIntDevSoc"));
        facebookPages.add(new FacebookPage("International Relations Society", "uccirsoc"));
        facebookPages.add(new FacebookPage("International Students Society", "ucc.internationals"));
        facebookPages.add(new FacebookPage("Islamic Society", "UCCIslamicSociety"));
        facebookPages.add(new FacebookPage("Italian Society", "UCCItalianSociety"));
        facebookPages.add(new FacebookPage("Japanese Society", "uccjapansoc"));
        facebookPages.add(new FacebookPage("Journalism Society", "UCCJournalismSociety"));
        facebookPages.add(new FacebookPage("Knitting Society", "UCCKnitSoc"));
        facebookPages.add(new FacebookPage("Korean Society", "ucckoreansociety"));
        facebookPages.add(new FacebookPage("Labour Society", "ucclabour"));
        facebookPages.add(new FacebookPage("Law Society", "ucclawsociety"));
        facebookPages.add(new FacebookPage("LGBT Society", "ucclgbt"));
        facebookPages.add(new FacebookPage("Macra Na Feirme", "uccmacra"));
        facebookPages.add(new FacebookPage("Management And Marketing Society", "MnMSocUCC"));
        facebookPages.add(new FacebookPage("Math Society", "uccmathsoc"));
        facebookPages.add(new FacebookPage("Mature Students Society", "UCCMSS"));
        facebookPages.add(new FacebookPage("Medical Society", "ucc.medsoc"));
        facebookPages.add(new FacebookPage("Medieval And Renaissance Society", "UCCMedRen"));
        facebookPages.add(new FacebookPage("Musical Society", "uccmusicalsociety"));
        facebookPages.add(new FacebookPage("Mythology Society", "UccMythology"));
        facebookPages.add(new FacebookPage("Networking Gaming and Technology Society", "UCCNetsoc"));
        facebookPages.add(new FacebookPage("Nursing And Midwifery Society", "UCCnursmidsoc"));
        facebookPages.add(new FacebookPage("Orchestra Society", "OrchestraUCC"));
        facebookPages.add(new FacebookPage("Pharmacy Society", "pharmsoc.ucc"));
        facebookPages.add(new FacebookPage("Philosophical Society", "uccphilosoph"));
        facebookPages.add(new FacebookPage("Photography Society", "uccphoto"));
        facebookPages.add(new FacebookPage("Physics And Astronomy Society", "uccphysoc"));
        facebookPages.add(new FacebookPage("Planning Society", "uccplanning"));
        facebookPages.add(new FacebookPage("Poker Society", "uccpokersociety"));
        facebookPages.add(new FacebookPage("Politics Society", "PolSocUCC"));
        facebookPages.add(new FacebookPage("Psychology Society", "110003839018417"));
        facebookPages.add(new FacebookPage("SAMH Society", "uccsamhsoc"));
        facebookPages.add(new FacebookPage("Science Society", "uccsciencesociety"));
        facebookPages.add(new FacebookPage("Scifi Society", "uccscifi"));
        facebookPages.add(new FacebookPage("Simon Society", "Uccsimonsociety"));
        facebookPages.add(new FacebookPage("Sinn Fein Society", "uccsinnfein"));
        facebookPages.add(new FacebookPage("Slainte Society", "184958275298"));
        facebookPages.add(new FacebookPage("Social Science Society", "uccsocsci"));
        facebookPages.add(new FacebookPage("Socialist Society", "uccsocialistyouth"));
        facebookPages.add(new FacebookPage("Sophia Society", "uccsophia"));
        facebookPages.add(new FacebookPage("South East Asia Society", "526366970861527"));
        facebookPages.add(new FacebookPage("SSDP Society", "UCCSSDP"));
        facebookPages.add(new FacebookPage("St. Vincent De Paul Society", "SVPUCC"));
        facebookPages.add(new FacebookPage("Suas Society", "uccsuas"));
        facebookPages.add(new FacebookPage("Surgeon Noonan Society", "1650501751859811"));
        facebookPages.add(new FacebookPage("Surgeon Society", "surgsoc"));
        facebookPages.add(new FacebookPage("Traditional Music Society", "ucctradsoc"));
        facebookPages.add(new FacebookPage("WARPS", "ucc.warps"));

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
