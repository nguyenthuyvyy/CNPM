import React, { useEffect, useState } from 'react';
import { View, FlatList, Button, Text } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { createOrder } from '../api/order';

export default function CartScreen({ token }) {
  const [cart, setCart] = useState([]);

  useEffect(() => {
    AsyncStorage.getItem('cart').then(c => setCart(JSON.parse(c) || []));
  }, []);

  const handleCheckout = async (method) => {
    if(cart.length===0){ alert('Cart empty'); return;}
    const order = {
      userId: 1,
      fullname: "Customer", 
      phone: "0000",
      address: "Address",
      items: cart.map(p=>({idProduct:p.id, quantity:p.quantity, price:p.price})),
      paymethod: method==="COD"?0:1,
      status:0,
      total: cart.reduce((sum,p)=>sum+p.price,0)
    };
    await createOrder(order, token);
    alert(`Order placed with ${method}`);
    setCart([]);
    AsyncStorage.removeItem('cart');
  };

  return (
    <View style={{flex:1}}>
      <FlatList
        data={cart}
        keyExtractor={(item,i)=>i.toString()}
        renderItem={({item}) => <Text>{item.name} x {item.quantity} - ${item.price}</Text>}
      />
      <Button title="Pay COD" onPress={()=>handleCheckout("COD")}/>
      <Button title="Pay Momo" onPress={()=>handleCheckout("Momo")}/>
    </View>
  );
}
