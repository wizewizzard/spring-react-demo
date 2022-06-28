import React, { useEffect, useState } from 'react'
import PostService from '../../service/PostService';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import PageRow from '../util/PageRow';
import Loader from '../Loader';
import ServerError from '../util/ServerError';

export default function PostList(){
  let [searchParams, setSearchParams] = useSearchParams();

  const page = searchParams.get('page') === null ? 1 : searchParams.get('page');  
  const [posts, setPosts] = useState();
  const navigate = useNavigate();
  const [loaded, setLoaded] = useState(false);
  const [paging, setPaging] = useState({
    pageSize: 6
    });
  const [serverError, setServerError] = useState(false);

  useEffect(() =>
      {
        fetchPosts();
      }, [searchParams]
  );
  
  const fetchPosts = async () => {
    const response = await PostService.getPosts({page: page - 1, pageSize: paging.pageSize });
    if(response.status >= 200 && response.status < 300){
      const data = await response.json();
      if(data.hasOwnProperty('_embedded')){
        setPosts(data._embedded.posts);
      }
      else{
        setPosts([]);
      }
      
      setPaging({
        ...paging,
        totalPages: data.page.totalPages
      });
      setLoaded(true);
    }
    else if(response.status >= 400 && response.status < 500){
      navigate('/404');
    }
    else if(response.status >= 500){
      setServerError(true);
    }
  }

  const renderComponent = () => {
    if(posts.length == 0) 
      return (
        <div className='container p-5'>
          <div className='text-center'>No posts</div>
        </div>
      )
    const oddColumn = [];
    const evenColumn = [];
    for(let i =0; i < posts.length; i++){
      if(i % 2 === 0) evenColumn.push(posts[i]);
      else oddColumn.push(posts[i]);
    }

    return (<div>
      <div className='container p-5'>
         <div className='row'>
             <PostsColumn key='even' posts={evenColumn} />
             <PostsColumn key = 'odd' posts = {oddColumn} />
         </div>
       </div> 
      {paging.totalPages > 1 &&
      <PageRow totalPages = {paging.totalPages} currentPage = {page} 
        onPageChange={pageClicked => {
          if(pageClicked != page){
            setLoaded(false);
            setSearchParams({page: pageClicked});
          }
        }}/>
      }
    </div>
  );
  }

  return ( serverError ? <ServerError message={'Server error occured. Unable to load posts.'}/> : loaded ? renderComponent() : <Loader />)
}

const PostBrief =  (props) => {  
  const post = props.post;
  return (
      <div className='row p-3 media post-brief'>
          <div className='col-3 p-2'><img className='scaled' src={post.imagePath} alt={'Post image'}/></div>
          <div className='col-9 p-2 media-body'>
              <h4>{<Link to={'./' + post.id} className='link-dark'>{post.title}</Link>}</h4>
              <div>{post.brief}</div>
          </div>
      </div>
  )
}

const PostsColumn = (props) => {
    let posts = props.posts;
    return (
        <div className='col-6'>
          {posts.map((p, i) => {
              return <PostBrief key={i} post={p}/>
          })}
        </div>
      )
}


