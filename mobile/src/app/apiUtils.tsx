import { BACKEND_URL } from "./constants";
import { dataRow } from "./constants";

/**
 * Function to get all posts from the backend
 * 
 * @param setData - Function to set the data state
 */
export const fetchData = async (setData: React.Dispatch<React.SetStateAction<dataRow[]>>) => {
  try {
    const response = await fetch(`${BACKEND_URL}/posts`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });
    
    if (!response.ok) {
      throw new Error(`Failed to retrieve posts. Status: ${response.status}`);
    }
    
    const result = await response.json();
    
    if (result.status !== "ok") {
      throw new Error("Backend returned an error status.");
    }
    
    setData(result.data);
  } catch (error) {
    console.error("Fetch Data Error:", error);
    
    // Provide mock data for testing in case of a failed request
    const mockData: dataRow[] = [
      { id: 1, title: "Mock Post 1", contents: "This is a mock post", upvotes: 10, downvotes: 2, is_liked: true, is_disliked: false},
      { id: 2, title: "Mock Post 2", contents: "Another mock post", upvotes: 5, downvotes: 1, is_liked: false, is_disliked: true},
    ];
    setData(mockData);
  }
};

/**
 * Function to update the upvote or downvote count of a post and refresh the data
 * @param item - The post to update
 * @param index - The index of the post in the data array
 * @param data - The current data array
 * @param setData - Function to set the data state
 * @param voteType - Either "upvote" or "downvote"
 */
export const toggleVote = async (
  item: dataRow,
  index: number,
  data: dataRow[],
  setData: React.Dispatch<React.SetStateAction<dataRow[]>>,
  voteType: "upvote" | "downvote"
) => {
  const newData = [...data];
  const isUpvote = voteType === "upvote";

  // Adjust the upvote or downvote count locally
  if (isUpvote) {
    newData[index].upvotes += 1;
  } else {
    newData[index].downvotes += 1;
  }

  setData(newData);

  try {
    const response = await fetch(`${BACKEND_URL}/posts/${item.id}/${voteType}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      throw new Error(`Failed to ${voteType} the post.`);
    }
  } catch (error) {
    console.error(`Toggle Vote Error (${voteType}):`, error);

    // Revert the change if the request fails
    if (isUpvote) {
      newData[index].upvotes -= 1;
    } else {
      newData[index].downvotes -= 1;
    }

    setData(newData);
    throw error;
  }
};

/**
 * Function to add a new post and refresh the data
 * 
 * @param newTitle - The title of the new post
 * @param newContents - The contents of the new post
 * @param data - The current data array
 * @param setData - Function to set the data state
 */
export const addPost = async (
  newTitle: string,
  newContents: string,
  data: dataRow[],
  setData: React.Dispatch<React.SetStateAction<dataRow[]>>
) => {
  const newPost = {
    title: newTitle,
    contents: newContents,
  };

  try {
    const response = await fetch(`${BACKEND_URL}/posts`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(newPost),
    });
    
    if (!response.ok) {
      throw new Error("Failed to add new post.");
    }

    fetchData(setData);
  } catch (error) {
    console.error("Add Post Error:", error);
    throw error;
  }
};
