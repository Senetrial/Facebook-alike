import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { CreatePost } from './CreatePost';
import * as api from '../api';

vi.mock('../api');

describe('CreatePost', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    // Mock document.getElementById for the file input reset logic
    const mockInput = { value: '' };
    vi.spyOn(document, 'getElementById').mockReturnValue(mockInput as any);
  });

  it('can create a post', async () => {
    const onPostCreated = vi.fn();
    (api.createPost as any).mockResolvedValue({
      id: '1',
      text: 'New Post',
      image: '',
      timestamp: new Date().toISOString()
    });

    render(<CreatePost onPostCreated={onPostCreated} />);

    const textarea = screen.getByPlaceholderText("What's on your mind?");
    const fileInput = screen.getByTestId('image-input');
    const button = screen.getByText('Post');

    fireEvent.change(textarea, { target: { value: 'New Post' } });
    
    // Simulate file upload
    const file = new File(['hello'], 'hello.png', { type: 'image/png' });
    fireEvent.change(fileInput, { target: { files: [file] } });

    fireEvent.click(button);

    await waitFor(() => {
      expect(api.createPost).toHaveBeenCalledWith('New Post', file);
    });

    await waitFor(() => {
      expect(onPostCreated).toHaveBeenCalled();
    });

    expect(textarea).toHaveValue('');
  });

  it('can create a post without an image', async () => {
    const onPostCreated = vi.fn();
    (api.createPost as any).mockResolvedValue({
      id: '2',
      text: 'Text Only Post',
      image: null,
      timestamp: new Date().toISOString()
    });

    render(<CreatePost onPostCreated={onPostCreated} />);

    const textarea = screen.getByPlaceholderText("What's on your mind?");
    const button = screen.getByText('Post');

    fireEvent.change(textarea, { target: { value: 'Text Only Post' } });
    
    expect(button).not.toBeDisabled();
    fireEvent.click(button);

    await waitFor(() => {
      expect(api.createPost).toHaveBeenCalledWith('Text Only Post', null);
    });

    await waitFor(() => {
      expect(onPostCreated).toHaveBeenCalled();
    });

    expect(textarea).toHaveValue('');
  });
});
