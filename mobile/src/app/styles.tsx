import { StyleSheet } from "react-native";

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
  },

  profileButtonContainer: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    paddingHorizontal: 20,
    paddingVertical: 10,
    backgroundColor: "#f0f0f0",
    borderRadius: 10,
    marginVertical: 10,
    marginHorizontal: 20,
  },

  banner: {
    width: "100%",
    height: 200,
    resizeMode: "cover",
  },

  header: {
    position: "relative",
    alignItems: "center",
    justifyContent: "center",
    paddingVertical: 20,
  },

  headerGradient: {
    position: "absolute",
    top: 0,
    width: "100%",
    height: "100%",
  },

  headerTitle: {
    fontSize: 24,
    fontWeight: "bold",
    color: "#fff",
    zIndex: 1,
  },

  addButton: {
    position: "absolute",
    right: 20,
    bottom: -25,
    backgroundColor: "#007AFF",
  },

  list: {
    paddingHorizontal: 20,
    paddingTop: 10,
  },

  listItem: {
    backgroundColor: "#fff",
    borderRadius: 8,
    padding: 15,
    marginVertical: 10,
    boxShadow: '0px 2px 8px rgba(0,0,0,0.1)',
    elevation: 2,
  },

  itemTitle: {
    fontSize: 18,
    fontWeight: "bold",
    marginBottom: 5,
  },

  voteContainer: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginTop: 10,
  },

  voteItem: {
    flexDirection: "row",
    alignItems: "center",
  },

  voteCount: {
    marginLeft: 5,
    fontSize: 16,
  },

  modalBackground: {
    flex: 1,
    backgroundColor: "rgba(0, 0, 0, 0.5)",
    justifyContent: "center",
    alignItems: "center",
  },

  errorContainer: {
    backgroundColor: '#fee2e2',
    padding: 16,
    margin: 16,
    borderRadius: 8,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  errorText: {
    color: "red",
    textAlign: "center",
    marginVertical: 10,
  },

  avatar: {
    width: 50,
    height: 50,
    borderRadius: 25,
    marginRight: 10,
  },
  listItemTextContainer: {
    flex: 1,
    justifyContent: "center",
  },
  postTitle: {
    fontSize: 16,
    fontWeight: "bold",
    marginBottom: 5,
  },
  postContents: {
    fontSize: 14,
    color: "gray",
  },
  likeButton: {
    alignSelf: "center",
  },
  dislikeButton: {
    alignSelf: "center",
  },
  card: {
    marginVertical: 10,
    borderRadius: 8,
    boxShadow: '0px 2px 4px rgba(0,0,0,0.1)',
    elevation: 2,
    backgroundColor: "#fff",
  },
  cardItem: {
    flexDirection: "row",
    alignItems: "center",
    padding: 15,
  },
});

export default styles;
