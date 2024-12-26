import React from 'react';
import { Modal, View, Text, TextInput, Button, StyleSheet } from 'react-native';

interface AddPostModalProps {
  modalVisible: boolean;
  setModalVisible: (visible: boolean) => void;
  newTitle: string;
  setNewTitle: (title: string) => void;
  newContents: string;
  setNewContents: (contents: string) => void;
  addPostFunction: () => void;
}

const AddPostModal: React.FC<AddPostModalProps> = ({
  modalVisible,
  setModalVisible,
  newTitle,
  setNewTitle,
  newContents,
  setNewContents,
  addPostFunction,
}) => {
  return (
    <Modal
      visible={modalVisible}
      animationType="slide"
      transparent={true}
      onRequestClose={() => setModalVisible(false)}
    >
      <View style={styles.modalBackground}>
        <View style={styles.modalContainer}>
          <Text style={styles.modalTitle}>New Post</Text>

          <TextInput
            placeholder="Title"
            value={newTitle}
            onChangeText={setNewTitle}
            style={styles.input}
          />
          <TextInput
            placeholder="Contents"
            value={newContents}
            onChangeText={setNewContents}
            style={styles.input}
            multiline
          />

          <Button title="Submit" onPress={addPostFunction} />
          <Button title="Cancel" onPress={() => setModalVisible(false)} />
        </View>
      </View>
    </Modal>
  );
};

const styles = StyleSheet.create({
  modalBackground: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
  },
  modalContainer: {
    width: 300,
    padding: 20,
    backgroundColor: '#fff',
    borderRadius: 8,
  },
  modalTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  input: {
    borderBottomWidth: 1,
    borderBottomColor: '#ccc',
    marginBottom: 20,
    padding: 10,
  },
});

export default AddPostModal;
