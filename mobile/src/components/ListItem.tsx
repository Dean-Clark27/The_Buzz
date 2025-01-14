// Import local dependencies
import styles from "../app/styles";
import { dataRow } from "../app/constants";
// Import dependencies
import { View, Text, Image } from "react-native";
import { Card, IconButton } from "react-native-paper";

/**
 * - ListItem component for displaying each post.
 * 
 * @param {Object} props - The properties of the component.
 * @param {Object} props.item - The post data.
 * @param {Function} props.toggleLikeFunction - Function to toggle the like status.
 * 
 * @returns {JSX.Element} - The ListItem component.
 */
export default function ListItem({
  item,
  toggleLikeFunction,
  toggleDislikeFunction,
}: {
  item: dataRow;
  toggleLikeFunction: () => void;
  toggleDislikeFunction: () => void;
}) {
  // Avatar component
  const Avatar = ({ source }: { source: any }) => (
    <Image source={source} style={styles.avatar} />
  );

  // ListItemText component
  const ListItemText = ({ title, contents }: { title: string; contents: string }) => (
    <View style={styles.listItemTextContainer}>
      <Text style={styles.postTitle}>{title}</Text>
      <Text style={styles.postContents}>{contents}</Text>
    </View>
  );

  // LikeButton component
  const LikeButton = ({ isLiked }: { isLiked: boolean }) => (
    <IconButton
      icon={isLiked ? "heart" : "thumbs-up-outline"}
      iconColor={isLiked ? "#ff5050" : "gray"}
      size={25}
      style={styles.likeButton}
      onPress={toggleLikeFunction}
      testID="LikeButton"
    />
  );

  // DislikeButton component
  const DislikeButton = ({ isDisliked }: { isDisliked: boolean }) => (
    <IconButton
      icon={isDisliked ? "thumb-down" : "thumb-down-outline"}
      iconColor={isDisliked ? "#5050ff" : "gray"}
      size={25}
      style={styles.dislikeButton}
      onPress={toggleDislikeFunction}
      testID="DislikeButton"
    />
  );

  // Return the card with the post data
  return (
    <Card style={styles.card}>
      <View style={styles.cardItem}>
        {/* Add an avatar */}
        <Avatar source={require("../assets/images/profile2.png")} />
        {/* Add the text container */}
        <ListItemText title={item.title} contents={item.contents} />
        {/* Add like and dislike buttons */}
        <View>
          <LikeButton isLiked={item.is_liked} />
          <DislikeButton isDisliked={item.is_disliked} />
        </View>
      </View>
    </Card>
  );
}