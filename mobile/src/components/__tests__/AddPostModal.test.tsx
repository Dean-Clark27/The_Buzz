import { render, fireEvent } from "@testing-library/react-native";
import AddPostModal from "../AddPostModal";

describe("AddPostModal", () => {
  const setModalVisible = jest.fn();
  const setNewTitle = jest.fn();
  const setNewContents = jest.fn();
  const addPostFunction = jest.fn();

  const props = {
    modalVisible: true,
    setModalVisible,
    newTitle: "Title",
    setNewTitle,
    newContents: "Contents",
    setNewContents,
    addPostFunction,
  };

  it("renders correctly", () => {
    const { queryByText, queryByDisplayValue } = render(<AddPostModal {...props} />);
    expect(queryByText("New Post")).not.toBeNull();
    expect(queryByDisplayValue("Title")).not.toBeNull();
    expect(queryByDisplayValue("Contents")).not.toBeNull();
  });

  it("calls setModalVisible properly on Cancel", () => {
    const { getByText } = render(<AddPostModal {...props} />);
    fireEvent.press(getByText("Cancel"));
    expect(setModalVisible).toHaveBeenCalledWith(false);
  });

  it("calls setModalVisible, addPostFunction, and clearInputFields properly on Submit", () => {
    const { getByText } = render(<AddPostModal {...props} />);
    fireEvent.press(getByText("Submit"));
    expect(setModalVisible).toHaveBeenCalledWith(false);
    expect(addPostFunction).toHaveBeenCalled();
    expect(setNewTitle).toHaveBeenCalledWith("");
    expect(setNewContents).toHaveBeenCalledWith("");
  });

  it("disables Submit button when newTitle or newContents is empty", () => {
    const { getByText } = render(<AddPostModal {...props} />);
    const submitButton = getByText("Submit");
    expect(submitButton.props.disabled).toBe(false);
  });

  it("enables Submit button when newTitle and newContents are not empty", () => {
    const { getByText } = render(<AddPostModal {...props} />);
    const submitButton = getByText("Submit");
    expect(submitButton.props.disabled).toBe(false);
  });
});