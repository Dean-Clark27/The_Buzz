// Backend URL
export const BACKEND_URL = "https://team-jailbreak.dokku.cse.lehigh.edu";

// Type for the data row
export type dataRow = {
  id: number;
  title: string;
  contents: string;
  upvotes: number;
  downvotes: number;
  is_liked: boolean;
  is_disliked: boolean;
};
