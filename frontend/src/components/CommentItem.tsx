import type { Comment } from '../types';

interface CommentItemProps {
  comment: Comment;
}

export const CommentItem = ({ comment }: CommentItemProps) => {
  return (
    <div style={{ padding: '8px', borderBottom: '1px solid #eee' }}>
      <p style={{ margin: 0 }}>{comment.text}</p>
      <small style={{ color: '#888' }}>
        {new Date(comment.timestamp).toLocaleString()}
      </small>
    </div>
  );
};
