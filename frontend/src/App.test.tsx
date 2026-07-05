import { render, screen,  waitFor, act } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import App from './App';
import * as api from './api';

vi.mock('./api');

const mockPost = {
  id: '1',
  text: 'Post 1',
  image: '',
  timestamp: new Date().toISOString()
};

describe('App', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    // Mock IntersectionObserver
    (global as any).IntersectionObserver = vi.fn(() => ({
      observe: vi.fn(),
      unobserve: vi.fn(),
      disconnect: vi.fn(),
    }));
  });

  it('renders title and fetches posts', async () => {
    (api.getPosts as any).mockResolvedValue({
      content: [mockPost],
      last: true,
      totalPages: 1,
      totalElements: 1,
      size: 3,
      number: 0
    });
    // Mock getComments since PostItem will call it
    (api.getComments as any).mockResolvedValue({
      content: [],
      last: true,
      totalPages: 1,
      totalElements: 0,
      size: 1,
      number: 0
    });

    render(<App />);

    expect(screen.getByText('Facebook Alike')).toBeInTheDocument();
    
    await waitFor(() => {
      expect(screen.getByText('Post 1')).toBeInTheDocument();
    });
  });

  it('loads more posts on scroll (mocked intersection)', async () => {
    let intersectCallback: any;
    (global as any).IntersectionObserver = vi.fn((callback) => {
      intersectCallback = callback;
      return {
        observe: vi.fn(),
        unobserve: vi.fn(),
        disconnect: vi.fn(),
      };
    });

    (api.getPosts as any)
      .mockResolvedValueOnce({
        content: [mockPost],
        last: false,
        totalPages: 2,
        totalElements: 2,
        size: 1,
        number: 0
      })
      .mockResolvedValueOnce({
        content: [{ ...mockPost, id: '2', text: 'Post 2' }],
        last: true,
        totalPages: 2,
        totalElements: 2,
        size: 1,
        number: 1
      });
    (api.getComments as any).mockResolvedValue({
      content: [],
      last: true,
      totalPages: 1,
      totalElements: 0,
      size: 1,
      number: 0
    });

    render(<App />);

    await waitFor(() => {
      expect(screen.getByText('Post 1')).toBeInTheDocument();
    });

    // Simulate intersection
    await act(async () => {
      intersectCallback([{ isIntersecting: true }]);
    });

    await waitFor(() => {
      expect(screen.getByText('Post 2')).toBeInTheDocument();
    });
    
    expect(screen.queryByText('Load More Posts')).not.toBeInTheDocument();
  });
});
