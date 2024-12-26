import React, { useState, useEffect } from "react";
import { View, Text, FlatList, Image, Button } from "react-native";
import { FAB } from "react-native-paper";
import { LinearGradient } from "expo-linear-gradient";
import Icon from "react-native-vector-icons/FontAwesome";
import { NavigationProp } from '@react-navigation/native';
import styles from "./styles";
import { dataRow } from "./constants";
import { fetchData, addPost, toggleVote } from "./apiUtils";
import AddPostModal from "../components/AddPostModal";

interface MessagesPageProps {
  user?: { name: string } | null; // Allow for null/undefined
  navigation: NavigationProp<any>;
}

interface User {
  token: string;
  name: string;
  email: string;
  picture: string;
}

interface VoteState {
  upvoted: boolean;
  downvoted: boolean;
}

export default function MessagesPage({ user = null, navigation }: MessagesPageProps) {
  const [data, setData] = useState<dataRow[]>([]);
  const [modalVisible, setModalVisible] = useState<boolean>(false);
  const [newTitle, setNewTitle] = useState<string>("");
  const [newContents, setNewContents] = useState<string>("");
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [voteStates, setVoteStates] = useState<Record<number, VoteState>>({});

  // Separate user greeting component with null check
  const UserGreeting = () => (
    <View style={styles.profileButtonContainer}>
      <Text>Welcome, {user ? user.name : 'Guest'}</Text>
      <Button 
        title="View Profile" 
        onPress={() => navigation.navigate('Profile')} 
        disabled={!user}
      />
    </View>
  );

  useEffect(() => {
    setVoteStates((prevStates) => {
      const newStates: Record<number, VoteState> = {};
      data.forEach((item) => {
        if (!prevStates[item.id]) {
          newStates[item.id] = { upvoted: false, downvoted: false };
        } else {
          newStates[item.id] = prevStates[item.id];
        }
      });
      return { ...prevStates, ...newStates };
    });
  }, [data]);  

  const handleVote = (item: dataRow, index: number, voteType: "upvote" | "downvote") => {
    if (!user) {
      setErrorMessage("Please log in to vote");
      return;
    }

    const isUpvote = voteType === "upvote";

    setVoteStates((prevState) => {
      const currentVoteState = prevState[item.id] || { upvoted: false, downvoted: false };
      let newUpvoted = currentVoteState.upvoted;
      let newDownvoted = currentVoteState.downvoted;

      if (isUpvote) {
        newUpvoted = !currentVoteState.upvoted;
        newDownvoted = false;
      } else {
        newDownvoted = !currentVoteState.downvoted;
        newUpvoted = false;
      }

      const updatedData = [...data];
      if (newUpvoted !== currentVoteState.upvoted) {
        updatedData[index].upvotes += newUpvoted ? 1 : -1;
      }
      if (newDownvoted !== currentVoteState.downvoted) {
        updatedData[index].downvotes += newDownvoted ? 1 : -1;
      }

      setData(updatedData);

      Promise.allSettled([
        newUpvoted !== currentVoteState.upvoted 
          ? toggleVote(item, index, updatedData, setData, "upvote")
          : Promise.resolve(),
        newDownvoted !== currentVoteState.downvoted 
          ? toggleVote(item, index, updatedData, setData, "downvote")
          : Promise.resolve(),
      ]).then((results) => {
        const failed = results.filter(res => res.status === "rejected");
        if (failed.length > 0) {
          setErrorMessage("Failed to update vote. Please try again.");
        }
      });

      Promise.all([
        toggleVote(item, index, updatedData, setData, "upvote"),
        toggleVote(item, index, updatedData, setData, "downvote"),
      ]).catch(() => {
        // Rollback state changes on error
        setData((prevData) => {
          const rollbackData = [...prevData];
          rollbackData[index].upvotes -= newUpvoted ? 1 : 0;
          rollbackData[index].downvotes -= newDownvoted ? 1 : 0;
          return rollbackData;
        });
      });
      

      return {
        ...prevState,
        [item.id]: { upvoted: newUpvoted, downvoted: newDownvoted }
      };
    });
  };

  const handleAddPost = async () => {
    if (!user) {
      setErrorMessage("Please log in to add a post");
      return;
    }

    try {
      await addPost(newTitle, newContents, data, setData);
      setModalVisible(false);
      setNewTitle("");
      setNewContents("");
    } catch (error) {
      console.error("Error adding post:", error);
      setErrorMessage("Failed to add new post. Please try again.");
    }
  };

  if (loading) {
    return (
      <View style={styles.container}>
        <Text>Loading...</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      {errorMessage && (
        <View style={styles.errorContainer}>
          <Text style={styles.errorText}>{errorMessage}</Text>
          <Button title="Dismiss" onPress={() => setErrorMessage(null)} />
        </View>
      )}

      <UserGreeting />

      <View>
        <Image
          source={require("../assets/images/banner2.png")}
          style={styles.banner}
          testID="Banner"
        />
        <View style={styles.header}>
          <LinearGradient
            colors={["rgba(255,255,255,0)", "rgba(0,0,0,0.9)"]}
            style={styles.headerGradient}
          />
          <Text style={styles.headerTitle}>Messages</Text>
          <FAB
            icon="plus"
            style={styles.addButton}
            onPress={() => setModalVisible(true)}
            testID="AddButton"
            disabled={!user}
          />
        </View>
      </View>

      <FlatList
        data={[...data].sort((a, b) => a.id - b.id)}
        renderItem={({ item, index }) => (
          <View style={styles.listItem}>
            <Text style={styles.itemTitle}>{item.title}</Text>
            <Text>{item.contents}</Text>
            <View style={styles.voteContainer}>
              <View style={styles.voteItem}>
                <Icon
                  name="arrow-up"
                  size={24}
                  color={voteStates[item.id]?.upvoted ? "blue" : "gray"}
                  onPress={() => handleVote(item, index, "upvote")}
                />
                <Text style={styles.voteCount}>{item.upvotes}</Text>
              </View>
              <View style={styles.voteItem}>
                <Icon
                  name="arrow-down"
                  size={24}
                  color={voteStates[item.id]?.downvoted ? "blue" : "gray"}
                  onPress={() => handleVote(item, index, "downvote")}
                />
                <Text style={styles.voteCount}>{item.downvotes}</Text>
              </View>
            </View>
          </View>
        )}
        keyExtractor={(item) => item.id.toString()}
        style={styles.list}
      />

      <AddPostModal
        modalVisible={modalVisible}
        setModalVisible={setModalVisible}
        newTitle={newTitle}
        setNewTitle={setNewTitle}
        newContents={newContents}
        setNewContents={setNewContents}
        addPostFunction={handleAddPost}
      />

      {modalVisible && <View style={styles.modalBackground} />}
    </View>
  );
}