import React from "react";
import Message from "./Message";
import Banner from "./Banner";
import profile from "../assets/img/profile.png";

export default function MessageContainer() {
  const url = import.meta.env.VITE_DOKKU_API_URL;
  const [messagesData, setMessagesData] = React.useState(); 
  const [refreshKey, setRefreshKey] = React.useState(0);

  React.useEffect(() => {
    async function getMessages() {
      const res = await fetch(`${url}/posts`); 
      const res_json = await res.json(); 
      setMessagesData(res_json.data?.sort((a, b) => a.id - b.id));
    }
    getMessages();
  }, [refreshKey]); 

  const triggerRefresh = () => {
    setRefreshKey((prevKey) => prevKey + 1); 
  };

  const messageElements = messagesData?.map(message => (
    <Message 
      key={message.id}
      messageId={message.id}
      image={profile}
      title={message.title}
      contents={message.contents}
      refresh={triggerRefresh}
      is_liked={message.is_liked}
    />
  ));

  return (
    <section className="content">
      <Banner />
      {messageElements}
    </section>
  );
}