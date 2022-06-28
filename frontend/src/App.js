import { BrowserRouter as Router, Route, Routes, useNavigate } from 'react-router-dom';

import Header from './components/Header';
import Home from './pages/Home';
import EditPost from './components/post/EditPost';
import PostDetail from './components/post/PostDetail';
import PostList from './components/post/PostList';
import About from './pages/About';
import NotFound from './pages/NotFound';

function App() {
  return (
    <Router>
      <div className='container'>
        <Header />
          <Routes>
            <Route path= "/" element={<Home />}></Route>
            <Route path= "/posts/:postId" element={<PostDetail />}></Route>
            <Route path= "/posts" element={<PostList />}></Route>
            <Route path= "/posts/create" element={<EditPost />}></Route>
            <Route path= "/posts/edit/:postId" element={<EditPost />}></Route>
            <Route path= "/about" element={<About />}></Route>
            <Route path= "/404" element={<NotFound />}></Route>
          </Routes>
      </div>
    </Router>
    
  );
}

export default App;
