import React, { useEffect, useState } from 'react';
import { View, FlatList, Button } from 'react-native';
import { getAllProducts } from '../api/product';
import ProductItem from '../components/ProductItem';

export default function HomeScreen({ navigation, token }) {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    getAllProducts().then(setProducts);
  }, []);

  return (
    <View style={{ flex:1 }}>
      <FlatList
        data={products}
        keyExtractor={item => item.id.toString()}
        renderItem={({item}) => <ProductItem product={item} />}
      />
      <Button title="Cart" onPress={() => navigation.navigate('Cart')}/>
      <Button title="Orders" onPress={() => navigation.navigate('Orders')}/>
    </View>
  );
}
