import React, { useEffect, useState } from 'react';
import { View, FlatList, Text } from 'react-native';
import { getOrders } from '../api/order';

export default function OrderHistoryScreen({ token }) {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    getOrders(token).then(setOrders);
  }, []);

  return (
    <View style={{flex:1}}>
      <FlatList
        data={orders}
        keyExtractor={item=>item.id.toString()}
        renderItem={({item}) => (
          <View style={{borderBottomWidth:1, padding:10}}>
            <Text>Order #{item.id} - Total: ${item.total}</Text>
            <Text>Status: {item.status}</Text>
          </View>
        )}
      />
    </View>
  );
}
