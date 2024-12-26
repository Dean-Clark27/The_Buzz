import React from "react";
import axios from "axios";

/**
 * AddMessageForm component allows users to submit a new message
 * by entering a title and content. The form is conditionally visible
 * based on the `isVisible` prop.
 *
 * @component
 * @param {Object} props - The component props.
 * @param {boolean} props.isVisible - Determines if the form should be displayed.
 * @param {Function} [props.refresh] - Optional function to refresh messages after submission.
 */
export default function AddMessageForm({ isVisible, refresh }) {
  /**
   * API URL for submitting the message.
   * @constant {string}
   */
  const url = import.meta.env.VITE_DOKKU_API_URL;

  /**
   * Form data state, holding the title and contents of the message.
   * @type {Object}
   * @property {string} title - The title of the message.
   * @property {string} contents - The contents of the message.
   */
  const [formData, setFormData] = React.useState({
    title: "",
    contents: "",
  });

  /**
   * Handles form input changes and updates the state.
   * 
   * @param {Object} event - The event object.
   */
  const handleChange = (event) => {
    setFormData((oldFormData) => ({
      ...oldFormData,
      [event.target.name]: event.target.value,
    }));
  };

  /**
   * Handles the form submission and sends the data to the API.
   *
   * @async
   * @param {Object} event - The event object.
   * @returns {Promise<void>}
   */
  async function handleSubmit(event) {
    event.preventDefault();

    try {
      const res = await fetch(`${url}/posts`, {
        method: "POST",
        headers: {
          "contents-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (!res.ok) {
        throw new Error("Network response was not ok");
      }

      // Reset the form data after successful submission
      setFormData({
        title: "",
        contents: "",
      });

      if (refresh) {
        refresh(); // Refresh the message list
      }
    } catch (error) {
      console.error("Error:", error);
    }
  }

  return (
    <>
      {isVisible && (
        <div className="add-form-container">
          <h2>Add Message</h2>
          <form onSubmit={handleSubmit}>
            <input
              className="form-input title-input"
              type="text"
              name="title"
              placeholder="Title"
              onChange={handleChange}
              value={formData.title}
              required
            />
            <textarea
              className="form-input contents-input"
              name="contents"
              placeholder="Message text..."
              onChange={handleChange}
              value={formData.contents}
              required
            />
            <div className="form-button-container">
              <button type="submit">Submit</button>
            </div>
          </form>
        </div>
        )
      }
    </>
  )
}