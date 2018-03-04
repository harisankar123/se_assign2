package com.example.demo;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.Region;

@Controller
public class ProfileController {
		@GetMapping(value="/")
		public ModelAndView renderPage() {
			ModelAndView indexpage= new ModelAndView();
			indexpage.setViewName("index");
			return indexpage;
			
		}
		@PostMapping(value="/upload")
		public ModelAndView uploadToS3( @RequestParam("file") MultipartFile image) {
			BasicAWSCredentials cred=new BasicAWSCredentials("AKIAIXAF6TBYBPQQYC6Q","QsBwvhdFNGJ0RiuuqgLB8ycfU1YdvQevWC9cBQey");
			ModelAndView profilepage= new ModelAndView();
			AmazonS3 s3client= AmazonS3ClientBuilder
					.standard()
					.withCredentials(new AWSStaticCredentialsProvider(cred))
					.withRegion(Regions.US_EAST_2)
					.build(); 
			try {
				PutObjectRequest putrq= new PutObjectRequest("harisankar",image.getOriginalFilename(),image.getInputStream(),new ObjectMetadata())
						.withCannedAcl(CannedAccessControlList.PublicRead);
				s3client.putObject(putrq);
				 String imgSrc="http://"+"harisankar"+".s3.amazonaws.com/"+image.getOriginalFilename();
				 profilepage.addObject("imgSrc", imgSrc);
				 profilepage.setViewName("profile");
				 return profilepage;
				 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				profilepage.setViewName("error");
				return profilepage;
			}
			
			
			
			
		}
}
