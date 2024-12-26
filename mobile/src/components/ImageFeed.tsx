import React from 'react';
import { FlatList, Image, Text, View, StyleSheet } from 'react-native';

// Define the type for each item in the list
interface ImageItem {
  imageUri: string;
  caption: string;
}

// Define the props type for the ImageFeed component
interface ImageFeedProps {
  data: ImageItem[]; // An array of ImageItem objects
}

const ImageFeed: React.FC<ImageFeedProps> = ({ data }) => {
  const renderItem = ({ item }: { item: ImageItem }) => (
    <View style={styles.item}>
      <Image source={{ uri: item.imageUri }} style={styles.image} />
      <Text style={styles.caption}>{item.caption}</Text>
    </View>
  );

  return (
    <FlatList
      data={data}
      keyExtractor={(item, index) => index.toString()}
      renderItem={renderItem}
    />
  );
};

const styles = StyleSheet.create({
  item: {
    marginBottom: 16,
    alignItems: 'center',
  },
  image: {
    width: 300,
    height: 200,
    borderRadius: 8,
  },
  caption: {
    marginTop: 8,
    fontSize: 16,
    color: '#333',
  },
});

export default ImageFeed;