import { BACKEND_URL } from "./constants";
import { dataRow } from "./constants";

/**
 * - Function to get all posts from the backend
 * 
 * @param setData - Function to set the data state
 * @throws - Will throw an error if the fetch operation fails.
 */
export const fetchData = async (setData: React.Dispatch<React.SetStateAction<dataRow[]>>) => {
  try {
    const response = await fetch(`${BACKEND_URL}/posts`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });
    // Check for a 200 OK response
    if (!response.ok) {
      throw new Error("Failed to retrieve posts");
    }
    // Extract the JSON from the response
    const result = await response.json();
    // Check if status is not ok
    if (result.status != "ok") {
      throw new Error("Failed to retrieve posts");
    }
    // Get the data from the JSON
    const data = result.data;
    // Set the local data to the fetched data
    setData(data);
  } catch (error) {
    console.error(error);
  }
};

/**
 * Function to update the like status of a post and refresh the data
 * 
 * @param item - The post to update
 * @param index - The index of the post in the data array
 * @param data - The current data array
 * @param setData - Function to set the data state
 * 
 * @throws - Will throw an error if the fetch operation fails.
 */
export const toggleLike = async (
  item: dataRow,
  index: number,
  data: dataRow[],
  setData: React.Dispatch<React.SetStateAction<dataRow[]>>
) => {
  // Copy data and get the old/new like status
  const newData = [...data];
  const oldLikeStatus = item.is_liked;
  const newLikeStatus = !oldLikeStatus;
  // Update the like status in the local data
  newData[index].is_liked = newLikeStatus;
  setData(newData);
  // Update the like status on the backend
  try {
    // Choose PUT or DELETE based on the new like status
    const method = newLikeStatus ? "POST" : "DELETE";
    // Make the request to the backend
    const response = await fetch(`${BACKEND_URL}/posts/${item.id}/like`, {
      method: method,
      headers: {
        "Content-Type": "application/json",
      },
    });
    // Check for a 200 OK response
    if (!response.ok) {
      throw new Error("Failed to update like status");
    }
    // If the backend succeeds, fetch the data again
    fetchData(setData);
    // If the backend fails, revert the like status in the local data
  } catch (error) {
    console.error(error);
    newData[index].is_liked = oldLikeStatus;
    setData(newData);
  }
};

/**
 * Function to add a new post and refresh the data
 * 
 * @param newTitle - The title of the new post
 * @param newContents - The contents of the new post
 * @param data - The current data array
 * @param setData - Function to set the data state
 * 
 * @throws - Will throw an error if the fetch operation fails.
 */
export const addPost = async (
  newTitle: string,
  newContents: string,
  data: dataRow[],
  setData: React.Dispatch<React.SetStateAction<dataRow[]>>,
) => {
  // Create a new post object
  const newPost = {
    title: newTitle,
    contents: newContents,
  };
  try {
    // Send a POST request to the backend
    const response = await fetch(`${BACKEND_URL}/posts`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(newPost),
    });
    // Check for a 201 Created response (backend should return 201, but 200 is fine i guess...)
    if (!response.ok) {
      throw new Error("Failed to add new post");
    }
    // Fetch the data again
    fetchData(setData);
  } catch (error) {
    console.error(error);
  }
};