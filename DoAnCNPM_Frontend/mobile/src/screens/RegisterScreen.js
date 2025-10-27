import React, { useState } from 'react';
import { View, TextInput, Button, StyleSheet } from 'react-native';
import { register } from '../api/auth';

export default function RegisterScreen({ navigation }) {
  const [fullname, setFullname] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [password, setPassword] = useState('');

  const handleRegister = async () => {
    try {
      await register(fullname, email, phone, password);
      alert('Register success');
      navigation.goBack();
    } catch (err) {
      alert('Register failed');
    }
  };

  return (
    <View style={styles.container}>
      <TextInput placeholder="Fullname" value={fullname} onChangeText={setFullname} style={styles.input}/>
      <TextInput placeholder="Email" value={email} onChangeText={setEmail} style={styles.input}/>
      <TextInput placeholder="Phone" value={phone} onChangeText={setPhone} style={styles.input}/>
      <TextInput placeholder="Password" value={password} onChangeText={setPassword} secureTextEntry style={styles.input}/>
      <Button title="Register" onPress={handleRegister}/>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex:1, justifyContent:'center', padding:20 },
  input: { borderWidth:1, marginBottom:10, padding:10, borderRadius:5 }
});
