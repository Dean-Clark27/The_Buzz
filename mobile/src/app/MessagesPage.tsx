// Import local dependencies
import styles from "./styles";
import { dataRow } from "./constants";
import { fetchData, toggleLike, addPost } from "./apiUtils";
import ListItem from "../components/ListItem";
import AddPostModal from "../components/AddPostModal";
// Import dependencies
import { useState, useEffect } from "react";
import { View, Text, FlatList, Image} from "react-native";
import { FAB } from "react-native-paper";
import { SafeAreaView } from "react-native-safe-area-context";
import { LinearGradient } from "expo-linear-gradient";

/**
 * Main component for the app
 * 
 * @returns - The main page component
 */
export default function MessagesPage() {
  const [data, setData] = useState<dataRow[]>([]); // Array of all posts
  const [modalVisible, setModalVisible] = useState<boolean>(false); // Modal visibility
  const [newTitle, setNewTitle] = useState<string>(""); // New post title
  const [newContents, setNewContents] = useState<string>(""); // New post contents

  // Fetch data on initial render
  useEffect(() => {
    fetchData(setData);
  }, []);
  
  return (
    // View to encompass the entire screen, also might consider using SafeAreaView (replace View with SafeAreaView to try it)
    <View style={styles.container}>
      {/* Wrapper for Banner and Header */}
      <View>
        {/* Top Banner Image */}
        <Image
          source={require("../assets/images/banner2.png")}
          style={styles.banner}
          testID="Banner"
        />
        {/* Header */}
        <View style={styles.header}>
          {/* Linear Gradient behind the Header */}
          <LinearGradient
            colors={["rgba(255,255,255,0)", "rgba(0,0,0,0.9)"]}

            style={styles.headerGradient}
          />
          {/* Header Text */}
          <Text style={styles.headerTitle}>Messages</Text>
          {/* Add Button */}
          <FAB
            icon="plus"
            style={styles.addButton}
            onPress={() => { setModalVisible(true); }}
            testID="AddButton"
          />
        </View>
      </View>

      {/* Post List using ListItem component */}
      <FlatList
        data={data.sort((a, b) => a.id - b.id)}
        renderItem={({ item, index }) => ( 
          <ListItem 
            item={item}
            toggleLikeFunction={() => toggleLike(item, index, data, setData)}
          />
        )}
        keyExtractor={item => item.id.toString()}
        style={styles.list}
      />

      {/* Modal for adding a new post */}
      <AddPostModal
        modalVisible={modalVisible}
        setModalVisible={setModalVisible}
        newTitle={newTitle}
        setNewTitle={setNewTitle}
        newContents={newContents}
        setNewContents={setNewContents}
        addPostFunction={() => addPost(newTitle, newContents, data, setData)}
      />
      {/* Supporting view to add a transparent overlay behind modal */}
      {modalVisible && <View style={styles.modalBackground} />}
    </View>
  );
}