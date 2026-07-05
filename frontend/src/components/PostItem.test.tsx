import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { PostItem } from './PostItem';
import * as api from '../api';

vi.mock('../api');

const mockPost = {
  id: '1',
  text: 'Test Post',
  image: '',
  timestamp: new Date().toISOString()
};

const mockComment = {
  id: 'c1',
  postId: '1',
  text: 'Test Comment',
  timestamp: new Date().toISOString()
};

describe('PostItem', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    (api.getLikes as any).mockResolvedValue({
      postId: '1',
      likes: 0,
      dislikes: 0
    });
  });

  it('renders post text, fetches comments and likes', async () => {
    (api.getComments as any).mockResolvedValue({
      content: [mockComment],
      last: true,
      totalPages: 1,
      totalElements: 1,
      size: 1,
      number: 0
    });
    (api.getLikes as any).mockResolvedValue({
      id: 'l1',
      postId: '1',
      likes: 10,
      dislikes: 2
    });

    render(<PostItem post={mockPost} />);

    expect(screen.getByText('Test Post')).toBeInTheDocument();
    
    await waitFor(() => {
      expect(screen.getByText('Test Comment')).toBeInTheDocument();
      expect(screen.getByText('10')).toBeInTheDocument();
      expect(screen.getByText('2')).toBeInTheDocument();
    });
  });

  it('can like and dislike a post', async () => {
    (api.getComments as any).mockResolvedValue({
      content: [],
      last: true,
      totalPages: 1,
      totalElements: 0,
      size: 1,
      number: 0
    });
    (api.getLikes as any).mockResolvedValue({
      postId: '1',
      likes: 0,
      dislikes: 0
    });
    (api.likePost as any).mockResolvedValue({
      postId: '1',
      likes: 1,
      dislikes: 0
    });
    (api.dislikePost as any).mockResolvedValue({
      postId: '1',
      likes: 1,
      dislikes: 1
    });

    render(<PostItem post={mockPost} />);

    const likeButton = await screen.findByTitle('Like');
    fireEvent.click(likeButton);

    await waitFor(() => {
      expect(api.likePost).toHaveBeenCalledWith('1');
      expect(screen.getByText('1')).toBeInTheDocument();
    });

    const dislikeButton = screen.getByTitle('Dislike');
    fireEvent.click(dislikeButton);

    await waitFor(() => {
      expect(api.dislikePost).toHaveBeenCalledWith('1');
      // Both likes and dislikes should show '1' now based on mocked return values
      const counts = screen.getAllByText('1');
      expect(counts.length).toBe(2);
    });
  });

  it('shows "+" button when there are more comments and loads them on click', async () => {
    (api.getComments as any)
      .mockResolvedValueOnce({
        content: [mockComment],
        last: false,
        totalPages: 2,
        totalElements: 2,
        size: 1,
        number: 0
      })
      .mockResolvedValueOnce({
        content: [{ ...mockComment, id: 'c2', text: 'Second Comment' }],
        last: true,
        totalPages: 2,
        totalElements: 2,
        size: 1,
        number: 1
      });

    render(<PostItem post={mockPost} />);

    const plusButton = await screen.findByText('+');
    fireEvent.click(plusButton);

    await waitFor(() => {
      expect(screen.getByText('Second Comment')).toBeInTheDocument();
    });
    
    expect(screen.queryByText('+')).not.toBeInTheDocument();

    // Verify "-" button rolls back comments
    (api.getComments as any).mockResolvedValueOnce({
      content: [mockComment],
      last: false,
      totalPages: 2,
      totalElements: 2,
      size: 1,
      number: 0
    });

    const minusButton = screen.getByText('-');
    fireEvent.click(minusButton);

    await waitFor(() => {
      expect(screen.queryByText('Second Comment')).not.toBeInTheDocument();
      expect(screen.getByText('Test Comment')).toBeInTheDocument();
      expect(screen.getByText('+')).toBeInTheDocument();
    });
  });

  it('can add a new comment', async () => {
    (api.getComments as any).mockResolvedValue({
      content: [],
      last: true,
      totalPages: 1,
      totalElements: 0,
      size: 1,
      number: 0
    });
    (api.createComment as any).mockResolvedValue({
      ...mockComment,
      text: 'New Comment'
    });

    render(<PostItem post={mockPost} />);

    const input = screen.getByPlaceholderText('Write a comment...');
    const button = screen.getByText('Post');

    fireEvent.change(input, { target: { value: 'New Comment' } });
    fireEvent.click(button);

    await waitFor(() => {
      expect(api.createComment).toHaveBeenCalledWith('1', 'New Comment');
    });

    await waitFor(() => {
      expect(input).toHaveValue('');
    });
  });

  it('does not render img tag when image is null or empty', async () => {
    (api.getComments as any).mockResolvedValue({
      content: [],
      last: true,
      totalPages: 1,
      totalElements: 0,
      size: 1,
      number: 0
    });

    const postWithoutImage = { ...mockPost, image: null };
    const { rerender } = render(<PostItem post={postWithoutImage} />);
    expect(screen.queryByRole('img', { name: 'Post' })).not.toBeInTheDocument();

    const postWithEmptyImage = { ...mockPost, image: '' };
    rerender(<PostItem post={postWithEmptyImage} />);
    expect(screen.queryByRole('img', { name: 'Post' })).not.toBeInTheDocument();
  });

  it('renders img tag when image is present', async () => {
    (api.getComments as any).mockResolvedValue({
      content: [],
      last: true,
      totalPages: 1,
      totalElements: 0,
      size: 1,
      number: 0
    });

    const postWithImage = { ...mockPost, image: 'base64data' };
    render(<PostItem post={postWithImage} />);
    
    expect(screen.getByRole('img', { name: 'Post' })).toBeInTheDocument();
    expect(screen.getByRole('img', { name: 'Post' })).toHaveAttribute('src', 'data:image/jpeg;base64,base64data');
  });
});
