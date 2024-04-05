import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Helper } from '../helper';
import { MessageService } from '../message.service';
import { FundBasketService } from '../fundbasket.service';
import { BlogService } from '../blog.service';
import { AuthenService } from '../authen.service';
import { Post } from '../post';
import { Location } from '@angular/common';
import { publishFacade } from '@angular/compiler';


@Component({
  selector: 'app-blog',
  templateUrl: './blog.component.html',
  styleUrl: './blog.component.css'
})

export class BlogComponent implements OnInit{
  selectedPost?: Post;
  user? : Helper;
  posts: Post[] = [];
  searchedPosts: Post[] = [];
  
  constructor( private blogService: BlogService, private authService: AuthenService,  private messageService: MessageService, private router: Router, private fundBasketService: FundBasketService,private location: Location, ){}


  ngOnInit(): void {
    console.log("initilizing")
    this.getPosts();
    this.authService.loadUser();
    this.load();
  }

  load(): void {
    console.log(`helper component loading...`);
    this.authService.exportUser().subscribe((newUser)=> {
      this.user=newUser;
    });
  }

  //selectedPost stuff
  onSelect(post: Post): void{
    this.selectedPost=post;
    this.messageService.add(`BlogComponent: Selected post id=${post.id}`);
  }


  hasAdmin(): boolean {
    if(this.user?.username=="admin") {
      return true;
    }
    else {
      return false;
    }
  }

  //POST need after checking for invalid vals
  add(description: string): void {
    description = description.trim();
    if (!this.user || !this.user.username || !description) { alert("Error: invalid field(s)") }
    else {
    let Op = this.user.username;
    let likes = 0;
    let id =0;
    this.blogService.addPost({ id:id, description:description, OPUsername:Op, likes:likes ,userLikes:[]} as Post)
      .subscribe(need => {
        this.posts.push(need);
      });
    }
  }
  
  //GET the whole needs list
  getPosts(): void {
    this.blogService.getPosts()
        .subscribe(posts => {
          this.posts = posts;
        });
  }

  // Checks if posts are linked to user
  checkUserPosts(post:Post):boolean{
    if(this.user?.username==post.OPUsername){
      return true;
    }
    return false;
  }

  // Checks if there are user posts are linked to user
  getUserPosts():Post[]{
    return this.posts.filter(post=>post.OPUsername===this.user?.username);
  }
  
  //GET a need specifically by ID (important for basket)
  getPostByID(textid: string): void {
    var id = parseInt(textid);
    if(id>0) {
    this.blogService.getPost(id)
      .subscribe(searchedPost =>this.searchedPosts=searchedPost? [searchedPost]:[]);
    }
    else {
      alert("Invalid ID: "+String(id))
    }
  }

  //used to add likes to a particular post
  likePost(post: Post): void{ 
    if (!post.userLikes) {
      post.userLikes = []; // Initialize userLikes as an empty array if it's null
    }
    post.likes++;
    if (this.user && this.user.username) {
      post.userLikes.push(this.user.username);
      this.blogService.updatePost(post).subscribe();
    }
    this.blogService.updatePost(post).subscribe(()=>{
      this.getPosts(); // Refresh the list of all posts
      this.authService.loadUser(); // Reload the user data
      this.load(); // Reload any necessary data
    });
  }

  // check if the presented post is liked by the current user
  checkLiked(post: Post):boolean{
    if (!this.user || !this.user.username||post.userLikes===null) {
      return false; // If user is not logged in, return false
    }
    return post.userLikes.includes(this.user.username);
  }

  //used to add likes to a particular post
  unlikePost(post: Post): void{ 
     if (!post.userLikes) {
    post.userLikes = []; // Initialize userLikes as an empty array if it's null
  }
  
  // Decrease the number of likes
  post.likes--;
  
  // Remove the user's like from userLikes array if the user is logged in
  if (this.user && this.user.username) {
    const index = post.userLikes.indexOf(this.user.username);
    if (index !== -1) {
      post.userLikes.splice(index, 1); // Remove the user's like from userLikes array
    }
  }
  
  // Update the post on the server
  this.blogService.updatePost(post).subscribe(()=>{
    this.getPosts(); // Refresh the list of all posts
    this.authService.loadUser(); // Reload the user data
    this.load(); // Reload any necessary data
  });
  }

  //Used to edit the discription of posts
  editPost(post:Post):void{
    if (post) {
      const newDescription = prompt("Enter new description:", post.description.toString());
      if (newDescription !== null) {
        post.description = newDescription.trim();
        this.blogService.updatePost(post).subscribe();
      }
    }
  }

  //GET a need by Name (MVP searchbar)
  getPostByName(name: string): void {
    if(name!="") {
    this.blogService.getPostsByName(name)
      .subscribe(searchedPosts => this.searchedPosts=searchedPosts)
    }
    else {
      alert("Error: empty string")
    }
  }

  //REMOVE using ufundservice
  deletePost(post: Post): void {
    this.blogService.deletePost(post.id)
    .subscribe(() => {
      this.searchedPosts = this.searchedPosts.filter(p => p.id !== post.id);
      this.getPosts(); // Refresh the list of all posts
      this.authService.loadUser(); // Reload the user data
      this.load(); // Reload any necessary data
  },);
    
  }

  goHome(): void {
    this.location.back();
  }
}
