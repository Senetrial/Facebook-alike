import { useState, useEffect } from 'react';
import type { Post, Comment, Like } from '../types';
import { getComments, createComment, getLikes, likePost, dislikePost } from '../api';
import { CommentItem } from './CommentItem';

interface PostItemProps {
  post: Post;
}

export const PostItem = ({ post }: PostItemProps) => {
  const [comments, setComments] = useState<Comment[]>([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(false);
  const [newComment, setNewComment] = useState('');
  const [likeData, setLikeData] = useState<Like | null>(null);

  const fetchComments = async (pageToFetch: number, clear = false) => {
    try {
      const data = await getComments(post.id, pageToFetch, 1);
      if (clear) {
        setComments(data.content);
        setPage(0);
      } else {
        setComments(prev => [...prev, ...data.content]);
      }
      setHasMore(!data.last);
    } catch (error) {
      console.error('Failed to fetch comments', error);
    }
  };

  const fetchLikes = async () => {
    try {
      const data = await getLikes(post.id);
      setLikeData(data);
    } catch (error) {
      console.error('Failed to fetch likes', error);
    }
  };

  useEffect(() => {
    fetchComments(0, true);
    fetchLikes();
  }, [post.id]);

  const handleLike = async () => {
    try {
      const updated = await likePost(post.id);
      setLikeData(updated);
    } catch (error) {
      console.error('Failed to like post', error);
    }
  };

  const handleDislike = async () => {
    try {
      const updated = await dislikePost(post.id);
      setLikeData(updated);
    } catch (error) {
      console.error('Failed to dislike post', error);
    }
  };

  const handleAddComment = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newComment.trim()) return;
    try {
      await createComment(post.id, newComment);
      setNewComment('');
      fetchComments(0, true);
    } catch (error) {
      console.error('Failed to add comment', error);
    }
  };

  const handleLoadMoreComments = () => {
    const nextPage = page + 1;
    setPage(nextPage);
    fetchComments(nextPage);
  };

  const handleShowLessComments = () => {
    fetchComments(0, true);
  };

  return (
    <div style={{ 
      border: '1px solid #ddd', 
      borderRadius: '8px', 
      marginBottom: '20px', 
      padding: '16px',
      backgroundColor: 'white'
    }}>
      <div style={{ marginBottom: '12px' }}>
        <p style={{ fontSize: '1.2rem', fontWeight: 'bold' }}>{post.text}</p>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <small style={{ color: '#888' }}>
            {new Date(post.timestamp).toLocaleString()}
          </small>
          <div style={{ display: 'flex', gap: '12px', fontSize: '1.1rem' }}>
            <span 
              onClick={handleLike} 
              style={{ cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '4px' }}
              title="Like"
            >
              👍 <span>{likeData?.likes || 0}</span>
            </span>
            <span 
              onClick={handleDislike} 
              style={{ cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '4px' }}
              title="Dislike"
            >
              👎 <span>{likeData?.dislikes || 0}</span>
            </span>
          </div>
        </div>
      </div>
      
      {post.image && (
        <img 
          src={`data:image/jpeg;base64,${post.image}`} 
          alt="Post" 
          style={{ width: '100%', borderRadius: '4px', marginBottom: '12px' }} 
        />
      )}

      <div style={{ borderTop: '1px solid #eee', paddingTop: '12px' }}>
        <h4>Comments</h4>
        {comments.map(comment => (
          <CommentItem key={comment.id} comment={comment} />
        ))}
        
        <div style={{ display: 'flex', gap: '8px', marginTop: '8px' }}>
          {hasMore && (
            <button 
              onClick={handleLoadMoreComments}
              style={{ 
                cursor: 'pointer',
                background: 'none',
                border: '1px solid #ddd',
                borderRadius: '50%',
                width: '30px',
                height: '30px',
                fontSize: '20px',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center'
              }}
            >
              +
            </button>
          )}

          {page > 0 && (
            <button 
              onClick={handleShowLessComments}
              style={{ 
                cursor: 'pointer',
                background: 'none',
                border: '1px solid #ddd',
                borderRadius: '50%',
                width: '30px',
                height: '30px',
                fontSize: '20px',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center'
              }}
            >
              -
            </button>
          )}
        </div>

        <form onSubmit={handleAddComment} style={{ marginTop: '12px', display: 'flex', gap: '8px' }}>
          <input 
            type="text" 
            value={newComment}
            onChange={(e) => setNewComment(e.target.value)}
            placeholder="Write a comment..."
            style={{ flex: 1, padding: '8px', borderRadius: '4px', border: '1px solid #ddd' }}
          />
          <button 
            type="submit"
            style={{ 
              padding: '8px 16px', 
              borderRadius: '4px', 
              border: 'none', 
              backgroundColor: '#1877f2', 
              color: 'white',
              cursor: 'pointer'
            }}
          >
            Post
          </button>
        </form>
      </div>
    </div>
  );
};
