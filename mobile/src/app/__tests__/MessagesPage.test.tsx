import { render, fireEvent, waitFor } from "@testing-library/react-native";
import { fetchData, toggleVote, addPost } from "../apiUtils";
import MessagesPage from "../MessagesPage";

// Mock the fetchData, toggleVote, and addPost functions
jest.mock("../apiUtils", () => ({
  fetchData: jest.fn(),
  toggleVote: jest.fn(),
  addPost: jest.fn(),
}));

// Mock the data returned by fetchData
const mockData = [
  { id: 1, title: "Title 1", contents: "Contents 1", upvotes: 5, downvotes: 2 },
  { id: 2, title: "Title 2", contents: "Contents 2", upvotes: 3, downvotes: 1 },
];

// Tests for the MessagesPage component
describe("MessagesPage", () => {
  beforeEach(() => {
    (fetchData as jest.Mock).mockImplementation((setData) => setData(mockData));
  });

  it("should render the header and banner", () => {
    const { getByText, getByTestId } = render(<MessagesPage />);
    expect(getByText("Messages")).toBeTruthy();
    expect(getByTestId("Banner")).toBeTruthy();
  });

  it("should render the list of posts", async () => {
    const { getByText } = render(<MessagesPage />);
    await waitFor(() => {
      expect(getByText("Title 1")).toBeTruthy();
      expect(getByText("Contents 1")).toBeTruthy();
      expect(getByText("Title 2")).toBeTruthy();
      expect(getByText("Contents 2")).toBeTruthy();
    });
  });

  it("opens and closes the AddPostModal", async () => {
    const { getByText, queryByText, getByTestId } = render(<MessagesPage />);
    fireEvent.press(getByTestId("AddButton"));
    expect(queryByText("New Post")).toBeTruthy();
    fireEvent.press(getByText("Cancel"));
    expect(queryByText("New Post")).toBeNull();
  });

  it("calls addPost when the form is submitted", async () => {
    const { getByText, getByPlaceholderText, getByTestId } = render(<MessagesPage />);
    fireEvent.press(getByTestId("AddButton"));
    fireEvent.changeText(getByPlaceholderText("Title"), "New Title");
    fireEvent.changeText(getByPlaceholderText("Contents"), "New Contents");
    fireEvent.press(getByText("Submit"));
    expect(addPost).toHaveBeenCalledWith("New Title", "New Contents", mockData, expect.any(Function));
  });

  it("calls toggleVote with 'upvote' when the upvote button is pressed", async () => {
    const { getByText } = render(<MessagesPage />);
    await waitFor(() => {
      fireEvent.press(getByText("Upvote"));
    });
    expect(toggleVote).toHaveBeenCalledWith(
      mockData[0],
      0,
      mockData,
      expect.any(Function),
      "upvote"
    );
  });

  it("calls toggleVote with 'downvote' when the downvote button is pressed", async () => {
    const { getByText } = render(<MessagesPage />);
    await waitFor(() => {
      fireEvent.press(getByText("Downvote"));
    });
    expect(toggleVote).toHaveBeenCalledWith(
      mockData[0],
      0,
      mockData,
      expect.any(Function),
      "downvote"
    );
  });
});
