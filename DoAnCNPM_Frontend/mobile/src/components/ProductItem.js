import React from 'react';
import { View, Text, Button, StyleSheet } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';

export default function ProductItem({ product }) {
  const addToCart = async () => {
    const cart = JSON.parse(await AsyncStorage.getItem('cart')) || [];
    cart.push({...product, quantity:1});
    await AsyncStorage.setItem('cart', JSON.stringify(cart));
    alert('Added to cart');
  };

  return (
    <View style={styles.item}>
      <Text>{product.name}</Text>
      <Text>${product.price}</Text>
      <Button title="Add to cart" onPress={addToCart}/>
    </View>
  );
}

const styles = StyleSheet.create({
  item: { padding:10, borderBottomWidth:1 }
});
