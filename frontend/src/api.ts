import type { Post, Comment, PaginatedResponse, Like } from './types';

const BASE_URL = 'http://localhost:8080/api';

export const getPosts = async (page = 0, size = 3): Promise<PaginatedResponse<Post>> => {
  const response = await fetch(`${BASE_URL}/posts?page=${page}&size=${size}`);
  if (!response.ok) throw new Error('Failed to fetch posts');
  return response.json();
};

export const createPost = async (text: string, image: File | null): Promise<Post> => {
  const formData = new FormData();
  formData.append('text', text);
  if (image) {
    formData.append('image', image);
  }

  const response = await fetch(`${BASE_URL}/posts`, {
    method: 'POST',
    body: formData,
  });
  if (!response.ok) throw new Error('Failed to create post');
  return response.json();
};

export const getComments = async (postId: string, page = 0, size = 1): Promise<PaginatedResponse<Comment>> => {
  const response = await fetch(`${BASE_URL}/comments?postId=${postId}&page=${page}&size=${size}`);
  if (!response.ok) throw new Error('Failed to fetch comments');
  return response.json();
};

export const createComment = async (postId: string, text: string): Promise<Comment> => {
  const response = await fetch(`${BASE_URL}/comments`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ postId, text }),
  });
  if (!response.ok) throw new Error('Failed to create comment');
  return response.json();
};

export const getLikes = async (postId: string): Promise<Like> => {
  const response = await fetch(`${BASE_URL}/likes/${postId}`);
  if (!response.ok) throw new Error('Failed to fetch likes');
  return response.json();
};

export const likePost = async (postId: string): Promise<Like> => {
  const response = await fetch(`${BASE_URL}/likes/${postId}/like`, {
    method: 'POST',
  });
  if (!response.ok) throw new Error('Failed to like post');
  return response.json();
};

export const dislikePost = async (postId: string): Promise<Like> => {
  const response = await fetch(`${BASE_URL}/likes/${postId}/dislike`, {
    method: 'POST',
  });
  if (!response.ok) throw new Error('Failed to dislike post');
  return response.json();
};
