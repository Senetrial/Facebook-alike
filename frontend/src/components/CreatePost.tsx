import { useState } from 'react';
import { createPost } from '../api';

interface CreatePostProps {
  onPostCreated: () => void;
}

export const CreatePost = ({ onPostCreated }: CreatePostProps) => {
  const [text, setText] = useState('');
  const [image, setImage] = useState<File | null>(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!text) return;

    setLoading(true);
    try {
      await createPost(text, image);
      setText('');
      setImage(null);
      // Reset file input
      (document.getElementById('image-input') as HTMLInputElement).value = '';
      onPostCreated();
    } catch (error) {
      console.error('Failed to create post', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ 
      border: '1px solid #ddd', 
      borderRadius: '8px', 
      marginBottom: '20px', 
      padding: '16px',
      backgroundColor: 'white'
    }}>
      <h3>Create a Post</h3>
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: '12px' }}>
          <textarea 
            value={text}
            onChange={(e) => setText(e.target.value)}
            placeholder="What's on your mind?"
            style={{ width: '100%', padding: '8px', borderRadius: '4px', border: '1px solid #ddd', minHeight: '80px' }}
          />
        </div>
        <div style={{ marginBottom: '12px' }}>
          <input 
            id="image-input"
            data-testid="image-input"
            type="file" 
            accept="image/*"
            onChange={(e) => setImage(e.target.files?.[0] || null)}
          />
        </div>
        <button 
          type="submit" 
          disabled={loading || !text}
          style={{ 
            padding: '8px 16px', 
            borderRadius: '4px', 
            border: 'none', 
            backgroundColor: (loading || !text) ? '#ccc' : '#42b72a', 
            color: 'white',
            fontWeight: 'bold',
            cursor: 'pointer'
          }}
        >
          {loading ? 'Posting...' : 'Post'}
        </button>
      </form>
    </div>
  );
};
