import { render, fireEvent } from "@testing-library/react-native";
import ListItem from "../ListItem";
import { dataRow } from "../../app/constants";

describe("ListItem", () => {
  // Mock toggleLikeFunction
  const toggleLikeFunction = jest.fn();
  // Fake data
  const item: dataRow = {
    id: 1,
    title: "Title",
    contents: "Contents",
    is_liked: false,
  };
  // Props
  const props = {
    item,
    toggleLikeFunction,
  };

  it("renders text correctly", () => {
    const { getByText } = render(<ListItem {...props} />);
    expect(getByText("Title")).toBeTruthy();
    expect(getByText("Contents")).toBeTruthy();
  });

  it("calls toggleLikeFunction when Like button is pressed", () => {
    const { getByTestId } = render(<ListItem {...props} />);
    fireEvent.press(getByTestId("LikeButton"));
    expect(toggleLikeFunction).toHaveBeenCalled();
  });
});