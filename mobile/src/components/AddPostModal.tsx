// Import local dependencies
import styles from "../app/styles";
// Import dependencies
import React from "react";
import { View, Text, TextInput, Modal, Button } from "react-native";

/**
 * - A modal component for adding a new post.
 *
 * @param {Object} props - The properties of the component.
 * @param {boolean} props.modalVisible - Determines if the modal is visible.
 * @param {Function} props.setModalVisible - Function to set the visibility of the modal.
 * @param {string} props.newTitle - The title of the new post.
 * @param {Function} props.setNewTitle - Function to set the title of the new post.
 * @param {string} props.newContents - The contents of the new post.
 * @param {Function} props.setNewContents - Function to set the contents of the new post.
 * @param {Function} props.addPostFunction - Function to add the new post.
 *
 * @returns {JSX.Element} - The AddPostModal component.
 */
export default function AddPostModal({
  modalVisible,
  setModalVisible,
  newTitle,
  setNewTitle,
  newContents,
  setNewContents,
  addPostFunction,
}: {
  modalVisible: boolean;
  setModalVisible: (visible: boolean) => void;
  newTitle: string;
  setNewTitle: (text: string) => void;
  newContents: string;
  setNewContents: (text: string) => void;
  addPostFunction: () => void;
}) {
  // Function to clear the input fields
  const clearInputFields = () => {
    setNewTitle("");
    setNewContents("");
  };

  // ModalHeader component
  const ModalHeader = () => (
    <View style={styles.modalHeader}>
      <Button title="Cancel" onPress={() => setModalVisible(false)} />
      <Text style={styles.newPostTitle}>New Post</Text>
      <Button
      title="Submit"
      onPress={() => { setModalVisible(false); addPostFunction(); clearInputFields(); }}
      disabled={!newTitle.trim() || !newContents.trim()}
      />
    </View>
  );

  // Return the AddPostModal component
  return (
    <Modal
      visible={modalVisible}
      animationType="slide"
      transparent={true}
      onRequestClose={() => setModalVisible(false)}
    >
      <View style={styles.modalContainer}>
        <View style={styles.modalContent}>
          {/* Modal header bar: Cancle, New Post, Post */}
          <ModalHeader />
          {/* Title Input */}
          <TextInput
            value={newTitle}
            onChangeText={setNewTitle}
            style={styles.modalInput}
            placeholder="Title"
            placeholderTextColor="gray"
            autoFocus={true}
            maxLength={100}
          />
          {/* Contents Input */}
          <TextInput
            value={newContents}
            onChangeText={setNewContents}
            style={[styles.modalInput, styles.modalContentsInput]}
            placeholder="Contents"
            placeholderTextColor="gray"
            multiline={true}
            maxLength={300}
          />
        </View>
      </View>
    </Modal>
  );
}