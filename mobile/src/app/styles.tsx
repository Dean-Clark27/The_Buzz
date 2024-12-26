import { StyleSheet } from 'react-native';

// Banner
const BANNER_HEIGHT = 230;
// Header
const HEADER_HEIGHT = 80;
const GRADIENT_HEIGHT = BANNER_HEIGHT*3/4;
// Padding
const LIST_ELEMENT_PADDING = 4;
const GENERAL_PADDING = 12;
// Avatar
const AVATAR_SIZE = 55;
const AVATAR_RADIUS = 10;
// Modal
const MODAL_BORDER_RADIUS = 10;

const styles = StyleSheet.create({
  // Container
  container: {
    // Use flex: 1 to make sure the container takes up the whole screen
    flex: 1,
    // This basically controls the entire background color of the screen
    backgroundColor: "#dadada",
  },

  // ~~~~~ Banner & Header ~~~~~
  // Banner
  banner: {
    width: "100%",
    height: BANNER_HEIGHT,
  },
  // Header
  header: {
    paddingHorizontal: GENERAL_PADDING,
    // Set the height to fit the contained elements
    height: HEADER_HEIGHT,
    // Layer onto the bottom of the banner
    position: "absolute",
    bottom: 0,
    left: 0,
    right: 0,
    // Align items in a row
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
  },

  // ~~~~~ Header Elements ~~~~~
  headerGradient: {
    position: "absolute",
    bottom: 0,
    left: 0,
    right: 0,
    // set the height to the same height as the header
    height: GRADIENT_HEIGHT,
  },
  headerTitle: {
    fontSize: 40,
    fontWeight: "bold",
    color: "white",
  },
  addButton: {
    // purple
    backgroundColor: "#f8a8f8",
    // yellow
    //backgroundColor: "#efc11a",
  },

  // ~~~~~ List ~~~~~
  list: {
    margin: LIST_ELEMENT_PADDING,
  },
  // Card element
  card: {
    margin: LIST_ELEMENT_PADDING,
  },
  cardItem: {
    flexDirection: "row",
    alignItems: "center",
    padding: GENERAL_PADDING,
  },

  // ~~~~~ Message Elements ~~~~~
  avatar: {
    width: AVATAR_SIZE,
    height: AVATAR_SIZE,
    borderRadius: AVATAR_RADIUS,
    marginRight: GENERAL_PADDING,
    // align to the top of the card
    alignSelf: "flex-start",
  },
  listItemTextContainer: {
    flex: 1,
  },
  postTitle: {
    fontSize: 18,
    fontWeight: "bold",
  },
  postContents: {
    fontSize: 14,
  },
  likeButton: {
    // align to the top of the card
    //alignSelf: "flex-start",
  },

  // ~~~~~ Modal Elements ~~~~~
  // Overall container for the modal
  modalContainer: {
    flex: 1,
    justifyContent: "flex-end",
    alignItems: "center",
  },
  // Content of the modal
  modalContent: {
    width: "100%",
    height: "90%",
    alignItems: "center",
    backgroundColor: "white",
    borderRadius: MODAL_BORDER_RADIUS,
  },
  // Header: Cancel, New Post, Post
  modalHeader: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    width: "100%",
    padding: GENERAL_PADDING,
    borderTopLeftRadius: MODAL_BORDER_RADIUS,
    borderTopRightRadius: MODAL_BORDER_RADIUS,
    backgroundColor: "#e0e0e0",
    // Small divider between header and inputs
    borderBottomColor: "#cccccc",
    borderBottomWidth: 1,
  },
  // "New Post" title
  newPostTitle: {
    fontSize: 16,
    textAlign: "center",
    fontWeight: "bold",
  },
  // Input fields
  modalInput: {
    fontSize: 16,
    width: "100%",
    padding: GENERAL_PADDING,
    // Small divider between inputs
    borderColor: "#cccccc",
    borderBottomWidth: 1,
  },
  modalContentsInput: {
    height: 200,
  },
  // Semi-transparent background for the modal
  modalBackground: {
    position: "absolute",
    width: "100%",
    height: "100%",
    backgroundColor: "rgba(0, 0, 0, 0.8)",
  },
});

export default styles;