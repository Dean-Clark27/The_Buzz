import React, { useState } from "react";

// Add type declaration for import.meta
declare global {
  interface ImportMeta {
    env: {
      VITE_DOKKU_API_URL: string;
    }
  }
}

interface MessageProps {
  messageId: string;
  image: string;
  title: string;
  contents: string;
  is_liked: boolean;
  refresh: () => void;
}

export default function Message({ messageId, image, title, contents, is_liked, refresh }: MessageProps) {
  const url = import.meta.env.VITE_DOKKU_API_URL;
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    title: title,
    contents: contents,
  });
  const [liked, setLiked] = useState(is_liked);

  const handleChange = (event: { target: { name: string; value: string } }) => {
    setFormData({
      ...formData,
      [event.target.name]: event.target.value,
    });
  };
  
  const handleEditClick = () => setIsEditing(true);
  const handleCancelClick = () => {
    setFormData({ title, contents });
    setIsEditing(false);
  };

  const handleApplyClick = async () => {
    try {
      const response = await fetch(`${url}/posts/${messageId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) throw new Error("Failed to update message");
      refresh();
      setIsEditing(false);
    } catch (error) {
      console.error("Error updating message:", error);
    }
  };

  const handleLikeClick = async () => {
    try {
      const response = await fetch(`${url}/posts/${messageId}/like`, {
        method: liked ? "DELETE" : "POST",
      });

      if (!response.ok) throw new Error(`Failed to ${liked ? 'unlike' : 'like'} the post`);
      refresh();
      setLiked(!liked);
    } catch (error) {
      console.error("Error toggling like:", error);
    }
  };

  const handleDeleteClick = async () => {
    try {
      const response = await fetch(`${url}/posts/${messageId}`, {
        method: "DELETE",
      });

      if (!response.ok) throw new Error("Failed to delete message");
      alert("Message deleted successfully");
      refresh();
    } catch (error) {
      console.error("Error deleting message:", error);
    }
  };

  return (
    <div className="message">
      <div
        className="message-image"
        style={{ backgroundImage: `url(${image})` }}
      />
      <div className="message-text-container">
        {isEditing ? (
          <>
            <input
              className="edit-input title-input"
              type="text"
              name="title"
              value={formData.title}
              onChange={handleChange}
            />
            <textarea
              className="edit-input contents-input"
              name="contents"
              value={formData.contents}
              onChange={handleChange}
            />
          </>
        ) : (
          <>
            <h2 className="message-title">{title}</h2>
            <p className="message-contents">{contents}</p>
          </>
        )}
      </div>

      <button 
        className={`message-like-btn ${liked ? 'liked' : ''}`} 
        onClick={handleLikeClick}
      >
        {liked ? "♥" : "♡"}
      </button>

      <div className="message-buttons">
        {isEditing ? (
          <>
            <button className="apply-btn message-button" onClick={handleApplyClick}>
              Apply
            </button>
            <button className="cancel-btn message-button" onClick={handleCancelClick}>
              Cancel
            </button>
          </>
        ) : (
          <>
            <button className="edit-btn message-button" onClick={handleEditClick}>
              Edit
            </button>
            <button className="delete-btn message-button" onClick={handleDeleteClick}>
              Delete
            </button>
          </>
        )}
      </div>
    </div>
  );
}