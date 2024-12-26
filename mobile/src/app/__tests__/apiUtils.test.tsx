import { BACKEND_URL, dataRow } from "../constants";
import { fetchData, toggleLike, addPost } from "../apiUtils";

let setData: jest.Mock;
let mockData: dataRow[];
let mockFetchResponse: any;

beforeEach(() => {
  // Mock setData
  setData = jest.fn();
  // Mock data
  mockData = [
    { id: 1, title: "Title1", contents: "Contents1", is_liked: false },
    { id: 2, title: "Title2", contents: "Contents2", is_liked: true }
  ];
  // Mock fetch response
  mockFetchResponse = {
    "status": "ok",
    "data": mockData,
  };
});

// Tests for the fetchData function
describe("fetchData", () => {
  it("should fetch posts and set the data", async () => {
    // Mock setData
    const setData = jest.fn();
    // Mock fetch
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockFetchResponse),
      } as Response)
    );

    // Call fetchData
    await fetchData(setData);

    // Check that fetch was called with the correct arguments
    expect(fetch).toHaveBeenCalledWith(`${BACKEND_URL}/posts`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });
    // Check that setData was called with the correct arguments
    expect(setData).toHaveBeenCalledWith(mockData);
  });

  it("should log an error if the fetch fails", async () => {
    // Mock console.error
    console.error = jest.fn();
    // Mock setData
    const setData = jest.fn();
    // Mock fetch
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: false,
      } as Response)
    );

    // Call fetchData
    await fetchData(setData);

    // Check that console.error was called with the correct arguments
    expect(console.error).toHaveBeenCalledWith(new Error("Failed to retrieve posts"));
    // Check that setData was not called
    expect(setData).not.toHaveBeenCalled();
  });
});

// Tests for the toggleLike function
describe("toggleLike", () => {
  it("should toggle a like from false to true and call setData", async () => {
    // Mock setData
    const setData = jest.fn();
    // Fake data
    const index = 0;
    const item = mockData[0];
    // Mock fetch
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
      } as Response)
    );

    // Call toggleLike
    await toggleLike(item, index, mockData, setData);

    // Check that setData was initially called with the correct arguments
    expect(setData).toHaveBeenCalledWith([
      { id: 1, title: "Title1", contents: "Contents1", is_liked: true },
      { id: 2, title: "Title2", contents: "Contents2", is_liked: true }
    ]);
    // Check that fetch was called with the correct arguments
    expect(fetch).toHaveBeenCalledWith(`${BACKEND_URL}/posts/${item.id}/like`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    });
  });
  it("should toggle a like from true to false and call setData", async () => {
    // Mock setData
    const setData = jest.fn();
    // Fake data
    const index = 1;
    const item = mockData[1];
    // Mock fetch
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
      } as Response)
    );

    // Call toggleLike
    await toggleLike(item, index, mockData, setData);

    // Check that setData was initially called with the correct arguments
    expect(setData).toHaveBeenCalledWith([
      { id: 1, title: "Title1", contents: "Contents1", is_liked: false },
      { id: 2, title: "Title2", contents: "Contents2", is_liked: false }
    ]);
    // Check that fetch was called with the correct arguments
    expect(fetch).toHaveBeenCalledWith(`${BACKEND_URL}/posts/${item.id}/like`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
    });
  });
  it("should log an error if the fetch fails", async () => {
    // Mock fetchData
    const fetchData = jest.fn();
    // Mock console.error
    console.error = jest.fn();
    // Mock setData
    const setData = jest.fn();
    // Fake data
    const mockData = [
      { id: 1, title: "Title1", contents: "Contents1", is_liked: false },
      { id: 2, title: "Title2", contents: "Contents2", is_liked: true }
    ];
    const index = 0;
    const item = mockData[0];
    // Mock fetch
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: false,
      } as Response)
    );

    // Call toggleLike
    await toggleLike(item, index, mockData, setData);

    // Check that console.error was called with the correct arguments
    expect(console.error).toHaveBeenCalledWith(new Error("Failed to update like status"));
    // Check that setData was last called with the old data after the fetch fails
    expect(setData).toHaveBeenLastCalledWith([
      { id: 1, title: "Title1", contents: "Contents1", is_liked: false },
      { id: 2, title: "Title2", contents: "Contents2", is_liked: true }
    ]);
  });
});

// Tests for the addPost function
describe("addPost", () => {
  it("should add a new post and call setData", async () => {
    // Mock setData
    const setData = jest.fn();
    // Fake data
    const newTitle = "New Title";
    const newContents = "New Contents";
    // Mock fetch
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
      } as Response)
    );

    // Call addPost
    await addPost(newTitle, newContents, mockData, setData);

    // Check that fetch was called with the correct arguments
    expect(fetch).toHaveBeenCalledWith(`${BACKEND_URL}/posts`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        title: newTitle,
        contents: newContents,
      }),
    });
  });
  it("should log an error if the fetch fails", async () => {
    // Mock console.error
    console.error = jest.fn();
    // Mock setData
    const setData = jest.fn();
    // Fake data
    const newTitle = "New Title";
    const newContents = "New Contents";
    // Mock fetch
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: false,
      } as Response)
    );

    // Call addPost
    await addPost(newTitle, newContents, mockData, setData);

    // Check that console.error was called with the correct arguments
    expect(console.error).toHaveBeenCalledWith(new Error("Failed to add new post"));
  });
});