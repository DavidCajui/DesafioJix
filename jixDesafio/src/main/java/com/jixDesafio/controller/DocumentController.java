package com.jixDesafio.controller;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import com.jixDesafio.model.Document;
import com.jixDesafio.repository.DocumentRepository;



@Controller
public class DocumentController {
	
	
	@Autowired
	private DocumentRepository dc;
	
	@RequestMapping(value  = "/cadastrarDoc", method=RequestMethod.GET)
	    public String formCadastrar(){
	        return "/documents/formDoc";
	  }
	
	
	 @RequestMapping(value = "/cadastrarDoc", method=RequestMethod.POST)
	    public String formCadastrar(Document document,  @RequestParam MultipartFile file) throws FileNotFoundException, IOException{
		    document.setStatus("Em Processamento");
		    
		    var id = document.getId();
			String raiz = System.getProperty("user.dir");
			String directorio = "src/main/resources/uploads";
			String path = raiz + "/" + directorio;
		    document.setLinkfixo(path+"/"+ file.getOriginalFilename());
		    document.setNameDoc(file.getOriginalFilename());
		 	dc.save(document);
		 	
		 	this.upload(file, document);
		 	
		 	return "redirect:index";
	  }
	
	 @RequestMapping("/index")
	 public ModelAndView listaDocuments(){
		 ModelAndView mv = new ModelAndView("index");
		 Iterable<Document> documents = dc.findAll();
		 mv.addObject("documents", documents);
		 return mv;
	 }
		 
	 
		 @RequestMapping(value = "/upload", method=RequestMethod.POST)
		    public void upload(@RequestParam MultipartFile file, Document document) throws FileNotFoundException, IOException{
			 var id = document.getId();
			 
			 String raiz = System.getProperty("user.dir");
			 String directorio = "src/main/resources/uploads"+ "/"+ id;
			String path = raiz + "/" + directorio;
			//document.setNameDoc(file.getOriginalFilename());
			 if (!new File(path).exists()) {
		            new File(path).mkdir();
		        } 

			 	 if(!file.isEmpty()){
			            byte[] bytes = file.getBytes();
			            String filename = file.getOriginalFilename();
			        
			            BufferedOutputStream stream =new BufferedOutputStream(new FileOutputStream(new File(path + "/" + filename)));
			            stream.write(bytes);
			            stream.flush();
			            stream.close();
			        }

		}
	 
		  	
		@RequestMapping(value="/{id}", method=RequestMethod.GET)
		public ModelAndView detalhesDoc(@PathVariable("id") long id) throws IOException{
			Document doc = dc.getById(id);
			ModelAndView mv = new ModelAndView("documents/detalhesDoc");
			mv.addObject("Document", doc);
			
			 URL url = new URL(doc.getLinkfixo());
		        File file = new File("C:/"+ doc.getNameDoc());

		        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
		        FileOutputStream fos = new FileOutputStream(file);
		        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		        fos.close();
		        rbc.close();

			
			return mv;
		}
		

		
		 @RequestMapping("/deletar")
		 public String deletar(long id){
			 Document document = dc.findByid(id);
			 dc.delete(document);
			 return "redirect:/index";
		 }
		
//		 @RequestMapping("/download")
//		 public ModelAndView download(@PathVariable("id") long id, Document document) {
//			//var x  = document.getLinkfixo();
//			Document doc = dc.findByid(id);
//			ModelAndView mv = new ModelAndView("documents/detalhesDoc");
//			mv.addObject("Document",doc );
//			return mv;
//			// return "redirect:/index()";
//			
//		 }
//		
		
		        
		    
		
	
 }

	 


