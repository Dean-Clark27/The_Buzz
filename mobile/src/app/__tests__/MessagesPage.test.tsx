import { render, fireEvent, waitFor } from "@testing-library/react-native";
import { fetchData, toggleLike, addPost } from "../apiUtils";
import MessagesPage from "../MessagesPage";

// Mock the fetchData, toggleLike, and addPost functions
jest.mock("../apiUtils", () => ({
  fetchData: jest.fn(),
  toggleLike: jest.fn(),
  addPost: jest.fn(),
}));

// Mock the data returned by fetchData
const mockData = [
  { id: 1, title: "Title 1", contents: "Contents 1", is_liked: false },
  { id: 2, title: "Title 2", contents: "Contents 2", is_liked: true },
];

// Tests for the MessagePage component
describe("MessagePage", () => {
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
});