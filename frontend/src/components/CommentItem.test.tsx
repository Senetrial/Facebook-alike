import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { CommentItem } from './CommentItem';

describe('CommentItem', () => {
  it('renders comment text and formatted date', () => {
    const timestamp = '2024-07-04T12:00:00Z';
    const mockComment = {
      id: 'c1',
      postId: '1',
      text: 'Hello comment',
      timestamp
    };

    render(<CommentItem comment={mockComment} />);

    expect(screen.getByText('Hello comment')).toBeInTheDocument();
    // The exact format depends on locale, but checking if it's there
    expect(screen.getByText(/2024/)).toBeInTheDocument();
  });
});
