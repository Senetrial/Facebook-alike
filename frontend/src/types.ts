export interface Post {
  id: string;
  image: string | null; // Base64 string from backend
  text: string;
  timestamp: string;
}

export interface Comment {
  id: string;
  postId: string;
  text: string;
  timestamp: string;
}

export interface Like {
  id: string;
  postId: string;
  likes: number;
  dislikes: number;
}

export interface PaginatedResponse<T> {
  content: T[];
  last: boolean;
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}
