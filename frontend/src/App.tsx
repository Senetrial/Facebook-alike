import { useState, useEffect, useRef, useCallback } from 'react';
import type { Post } from './types';
import { getPosts } from './api';
import { PostItem } from './components/PostItem';
import { CreatePost } from './components/CreatePost';
import './App.css';

function App() {
  const [posts, setPosts] = useState<Post[]>([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);
  const observer = useRef<IntersectionObserver | null>(null);

  const lastPostElementRef = useCallback((node: HTMLDivElement) => {
    if (loading) return;
    if (observer.current) observer.current.disconnect();
    observer.current = new IntersectionObserver(entries => {
      if (entries[0].isIntersecting && hasMore) {
        setPage(prevPage => prevPage + 1);
      }
    });
    if (node) observer.current.observe(node);
  }, [loading, hasMore]);

  const fetchPosts = async (pageToFetch: number, clear = false) => {
    setLoading(true);
    try {
      const data = await getPosts(pageToFetch);
      if (clear) {
        setPosts(data.content);
        setPage(0);
      } else {
        setPosts(prev => [...prev, ...data.content]);
      }
      setHasMore(!data.last);
    } catch (error) {
      console.error('Failed to fetch posts', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPosts(0, true);
  }, []);

  useEffect(() => {
    if (page > 0) {
      fetchPosts(page);
    }
  }, [page]);

  const handlePostCreated = () => {
    if (page === 0) {
      fetchPosts(0, true);
    } else {
      setPage(0);
      fetchPosts(0, true);
    }
  };

  return (
    <div className="container">
      <header className="header">
        <h1>Facebook Alike</h1>
      </header>

      <main className="main">
        <CreatePost onPostCreated={handlePostCreated} />

        <div className="post-list">
          {posts.map((post, index) => {
            if (posts.length === index + 1) {
              return (
                <div ref={lastPostElementRef} key={post.id}>
                  <PostItem post={post} />
                </div>
              );
            } else {
              return <PostItem key={post.id} post={post} />;
            }
          })}
        </div>

        {loading && <p style={{ textAlign: 'center', padding: '20px' }}>Loading posts...</p>}
      </main>
    </div>
  );
}

export default App;
