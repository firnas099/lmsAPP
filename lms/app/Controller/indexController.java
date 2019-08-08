package com.lms.app.com.lms.app.Controller;

import com.lms.app.com.lms.app.model.*;

import com.lms.app.com.lms.app.repo.*;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class indexController {

    @Autowired
    studentRepo studentRepo;

    @Autowired
    lectureRepo lectureRepo;

    @Autowired
    eventRepo eventRepo;

    @Autowired
    subjectRepo subjectRepo;

    @Autowired
    marksRepo marksRepo;

    @Autowired
    PositiveRepo positiveRepo;

    @Autowired
    negativeRepo negativeRepo;

    @Autowired
     MiddleRepo middleRepo;
   



    @RequestMapping("/")
    public String adminPage(){

        return "index";
    }



    @RequestMapping("/showstudent")
    public String studentPage(Model model,@RequestParam(defaultValue = "0") int page){
        model.addAttribute("data",studentRepo.findAll(new PageRequest(page,3)));
        model.addAttribute("currentPage",page);

        return "addstudentdata";
    }

    @PostMapping("/savestudent")
    public String addStudent(Students students){

        studentRepo.save(students);
        return "redirect:/showstudent";

    }

    @GetMapping("/deletestudents")
    public String deleteStudent(int id){
         studentRepo.deleteById(id);
        return "redirect:/showstudent";

    }

    @GetMapping("/findstudents")
    @ResponseBody
    public Optional<Students> findeStudent(int id){
       return studentRepo.findById(id);


    }
    
    @GetMapping("/findone")
    @ResponseBody
    public Students findstude(int id) {
    	return studentRepo.getOne(id);
    }


    @RequestMapping("/adddata")
    public String addstdata(){
        return "addstudentdata";
    }



    @RequestMapping("/showlecture")
    public String lecturepage(Model model,@RequestParam(defaultValue = "0")int page){

        model.addAttribute("data",lectureRepo.findAll(new PageRequest(page,3)));
        model.addAttribute("currentPage",page);
        return "lecture";
    }

    @PostMapping("/addlecture")
    public String saveLactur(Lecture lecture){

       lectureRepo.save(lecture);
        return "redirect:/showlecture";

    }

    @GetMapping("/deletelecture")
    public  String deleteLecture(int id){

     lectureRepo.deleteById(id);
     return "redirect:/showlecture";
    }

    @GetMapping("/findlecture")
    @ResponseBody
    public Optional<Lecture> findLecture(int id){

      return   lectureRepo.findById(id);
    }

    @RequestMapping("/evnt")
    public  String showevents(){

        return "events";
    }

    @PostMapping("/addevent")
    public String addevents(Event event){

        eventRepo.save(event);

        return "redirect:/evnt";
    }


    @RequestMapping("/showsubject")
    public String showSubjectPage(){
        return "subject";
    }


    @PostMapping("/addsubjrct")
    public String addsubjecttoDb(subjects subjects){

        subjectRepo.save(subjects);

        return "redirect:/showsubject";
    }

    @RequestMapping("/logl")
    public  String logLecture(){

        return "logingl";
    }

    @RequestMapping("/leclog")
    public String lectureLog(Model model,int id){
         
    	
        List<subjects>subjects=subjectRepo.findByLid(id);
        
        
        Lecture lecture =lectureRepo.findBycCusId(id);
        model.addAttribute("events", eventRepo.findAll());
        model.addAttribute("data",subjects);
        model.addAttribute("ldata", lecture);
  
    
     
      
     


     return "lecturelog";
    }
    
    @RequestMapping("/getevnt")
    public String getEvents() {
    	
    	List<Event> event=eventRepo.findAll();
    	System.out.println(" events is"+event);
    	return"lecturelog";
    }
    @RequestMapping("/showmarks")
    private String  showMarksPage(Model model,@RequestParam(defaultValue = "0")int page) {
    	
    	model.addAttribute("marks",marksRepo.findAll(new PageRequest(page,3)));
    	model.addAttribute("currentpage",page);
    	
    	return "addmarks";
    	
    }

    @RequestMapping("/addstmarks")
    public String  addMarks(Marks marks){

      marksRepo.save(marks);

        return "redirect:/showmarks";
    }


    @GetMapping("/findmarks")
    @ResponseBody
    public Optional<Marks> findMarks(int marksid){

       return marksRepo.findById(marksid);


    }


    @GetMapping("/deletemarks")
    public String delMarks(int marksid){

       marksRepo.deleteById(marksid);

        return "redirect:/showmarks";
    }

  @RequestMapping("/stlg")
  public String studentLog(){

        return "logst";
  }

  @RequestMapping("/stdash")
  public String studentDashbord(int id,Model model){

  Students students=studentRepo.findByCustom(id);
      Marks marks=marksRepo.findBycustom(id);


  model.addAttribute("evnt",eventRepo.findAll());

  model.addAttribute("stu",students);
  model.addAttribute("mark",marks);


 System.out.print("marks is" +marks);


   return "studentlog";
  }

  @RequestMapping("/feedbk")
  public String showFeedback(){

      return "feedback";
  }


  @RequestMapping("/addfeed")
  public String addFeedback(@RequestParam int fid,String message,Positive positive,Negative negative,MiddleComments middleComments){

      StanfordCoreNLP stanfordCoreNLP=Pipeline.getPipeline();
      String text=message;
      CoreDocument coreDocument=new CoreDocument(text);
      stanfordCoreNLP.annotate(coreDocument);
      List<CoreSentence>sentences=  coreDocument.sentences();

     for (CoreSentence sentence:sentences){

         String sentiment=sentence.sentiment();
         System.out.println(sentiment +"= " +sentence);
         if (sentiment .equals("Positive")){

             System.out.println("positive comment");
             positiveRepo.save(positive);


         }
         else if (sentiment.equals("Negative")){

             System.out.println("negative comment");
             negativeRepo.save(negative);
         }
         else {
             System.out.println("middle comment");
             middleRepo.save(middleComments);

         }
     }

       return  "feedback";



  }

    @RequestMapping("/showfeed")
    public String showFeedack(Model model){


      List<Positive> positive=positiveRepo.findAll();
      model.addAttribute("feed",positive);
        System.out.println("feedback is "+positive);
        return "DBord";

    }

    @RequestMapping("/ss")
    public  String ss(){

     Positive positive=positiveRepo.findallbyCus();

        System.out.println(positive.getId());
        return "feedback";

    }





}
